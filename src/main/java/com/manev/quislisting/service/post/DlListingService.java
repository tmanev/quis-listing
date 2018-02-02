package com.manev.quislisting.service.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlListingContentFieldRel;
import com.manev.quislisting.domain.DlListingLocationRel;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.DlAttachmentRepository;
import com.manev.quislisting.repository.DlContentFieldItemRepository;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.search.DlListingSearchRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.security.AuthoritiesConstants;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.dto.ApproveDTO;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingFieldDTO;
import com.manev.quislisting.service.post.mapper.AttachmentMapper;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.qlml.LanguageService;
import com.manev.quislisting.service.storage.StorageService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.util.AttachmentUtil;
import com.manev.quislisting.service.util.SlugUtil;
import com.manev.quislisting.web.rest.filter.DlContentFieldFilter;
import com.manev.quislisting.web.rest.filter.DlListingSearchFilter;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DlListingService {

    private static final String USER_S_WAS_NOT_FOUND_IN_THE_DATABASE = "User : %s was not found in the database";
    public static final String TRANSLATED_LOCATIONS = "translatedLocations";
    public static final String TRANSLATED_LOCATIONS_ID = "translatedLocations.id";
    private static Clock clock = Clock.systemUTC();
    private final Logger log = LoggerFactory.getLogger(DlListingService.class);
    private final EmailSendingService emailSendingService;
    private DlListingRepository dlListingRepository;
    private UserRepository userRepository;
    private DlCategoryRepository dlCategoryRepository;
    private DlLocationRepository dlLocationRepository;
    private DlListingMapper dlListingMapper;
    private AttachmentMapper attachmentMapper;
    private StorageService storageService;
    private LanguageService languageService;
    private DlContentFieldRepository dlContentFieldRepository;
    private DlContentFieldItemRepository dlContentFieldItemRepository;
    private DlAttachmentRepository dlAttachmentRepository;
    private DlListingSearchRepository dlListingSearchRepository;
    private TranslationGroupRepository translationGroupRepository;

    public DlListingService(DlListingRepository dlListingRepository, UserRepository userRepository,
                            DlCategoryRepository dlCategoryRepository, DlLocationRepository dlLocationRepository,
                            DlListingMapper dlListingMapper, AttachmentMapper attachmentMapper,
                            StorageService storageService, LanguageService languageService,
                            DlContentFieldRepository dlContentFieldRepository,
                            DlContentFieldItemRepository dlContentFieldItemRepository,
                            DlAttachmentRepository dlAttachmentRepository, DlListingSearchRepository dlListingSearchRepository, EmailSendingService emailSendingService, TranslationGroupRepository translationGroupRepository) {
        this.dlListingRepository = dlListingRepository;
        this.userRepository = userRepository;
        this.dlCategoryRepository = dlCategoryRepository;
        this.dlLocationRepository = dlLocationRepository;
        this.dlListingMapper = dlListingMapper;
        this.attachmentMapper = attachmentMapper;
        this.storageService = storageService;
        this.languageService = languageService;
        this.dlContentFieldRepository = dlContentFieldRepository;
        this.dlContentFieldItemRepository = dlContentFieldItemRepository;
        this.dlAttachmentRepository = dlAttachmentRepository;
        this.dlListingSearchRepository = dlListingSearchRepository;
        this.emailSendingService = emailSendingService;
        this.translationGroupRepository = translationGroupRepository;
    }

    public DlListingDTO save(DlListingDTO dlListingDTO, String languageCode) {
        log.debug("Request to save DlListingDTO : {}", dlListingDTO);

        DlListing dlListingForSaving = getDlListingForSaving(dlListingDTO, languageCode, null);

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing, languageCode);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO saveAndPublish(DlListingDTO dlListingDTO, String languageCode) {
        log.debug("Request to save DlListingDTO : {}", dlListingDTO);

        DlListing dlListingForSaving = getDlListingForSaving(dlListingDTO, languageCode, null);
        dlListingForSaving.setStatus(DlListing.Status.PUBLISHED);

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);
        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(dlListingForSaving, null);
        dlListingSearchRepository.save(savedDlListingDTO);

        emailSendingService.sendPublishedNotification(savedDlListing);
        // send to user that publication is published
        return savedDlListingDTO;
    }

    public void publishListing(Long id) {
        DlListing dlListingForSaving = dlListingRepository.findOne(id);
        dlListingForSaving.setStatus(DlListing.Status.PUBLISHED);
        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);
        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing, null);
        dlListingSearchRepository.save(savedDlListingDTO);
    }

    public DlListingDTO approveListing(Long id) {
        DlListing dlListing = dlListingRepository.findOne(id);

        dlListing.setApproved(Boolean.TRUE);
        dlListing.setApprovedModified(new Timestamp(clock.millis()));
        DlListing save = dlListingRepository.save(dlListing);

        DlListingDTO dlListingDTO = dlListingMapper.dlListingToDlListingDTO(save, null);
        dlListingSearchRepository.save(dlListingDTO);

        return dlListingDTO;
    }

    public DlListingDTO disapproveListing(Long id, ApproveDTO approveDTO) {
        DlListing dlListing = dlListingRepository.findOne(id);

        dlListing.setApproved(Boolean.FALSE);
        dlListing.setApprovedModified(new Timestamp(clock.millis()));
        dlListing.setStatus(DlListing.Status.PUBLISH_DISAPPROVED);
        DlListing save = dlListingRepository.save(dlListing);

        DlListingDTO dlListingDTO = dlListingMapper.dlListingToDlListingDTO(save, null);
        dlListingSearchRepository.save(dlListingDTO);

        emailSendingService.sendListingDisapprovedEmail(save, approveDTO.getMessage());

        return dlListingDTO;
    }

    private DlListing getDlListingForSaving(DlListingDTO dlListingDTO, String langKey, Long translationGroupId) {
        DlListing dlListingForSaving;
        Timestamp now = new Timestamp(clock.millis());
        if (dlListingDTO.getId() != null) {
            dlListingForSaving = dlListingRepository.findOne(dlListingDTO.getId());

            setCommonProperties(dlListingDTO, dlListingForSaving);
            dlListingForSaving.setModified(now);
        } else {
            String currentUserLogin = SecurityUtils.getCurrentUserLogin();
            Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin);
            if (oneByLogin.isPresent()) {
                dlListingForSaving = new DlListing();

                setCommonProperties(dlListingDTO, dlListingForSaving);

                TranslationGroup translationGroup = getTranslationGroup(translationGroupId);
                dlListingForSaving.setTranslation(
                        TranslationBuilder.aTranslation()
                                .withLanguageCode(langKey)
                                .withTranslationGroup(translationGroup)
                                .build());

                dlListingForSaving.setCreated(now);
                dlListingForSaving.setModified(now);
                dlListingForSaving.setUser(oneByLogin.get());
            } else {
                throw new UsernameNotFoundException(String.format(USER_S_WAS_NOT_FOUND_IN_THE_DATABASE, currentUserLogin));
            }
        }
        return dlListingForSaving;
    }

    private TranslationGroup getTranslationGroup(Long translationGroupId) {
        if (translationGroupId != null) {
            return translationGroupRepository.findOne(translationGroupId);
        } else {
            return new TranslationGroup();
        }
    }

    private void setCommonProperties(DlListingDTO dlListingDTO, DlListing dlListingForSaving) {
        dlListingForSaving.setTitle(dlListingDTO.getTitle());
        dlListingForSaving.setName(SlugUtil.getFileNameSlug(dlListingDTO.getTitle()));
        dlListingForSaving.setContent(dlListingDTO.getContent());

        // if user is in role ADMIN don't change the status
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) || dlListingForSaving.getStatus() == null) {
            dlListingForSaving.setStatus(DlListing.Status.DRAFT);
        }

        dlListingForSaving.setApproved(null);
        dlListingForSaving.setApprovedModified(null);

        setCategory(dlListingDTO, dlListingForSaving);
        setLocation(dlListingDTO, dlListingForSaving);
        setFeaturedAttachment(dlListingDTO, dlListingForSaving);

        List<DlListingFieldDTO> dlListingFieldDTOS = dlListingDTO.getDlListingFields();

        if (dlListingFieldDTOS != null) {
            Set<DlListingContentFieldRel> existingDlListingContentFieldRels = dlListingForSaving.getDlListingContentFieldRels();
            for (DlListingFieldDTO dlListingFieldDTO : dlListingFieldDTOS) {
                DlContentField dlContentField = dlContentFieldRepository.findOne(dlListingFieldDTO.getId());
                DlListingContentFieldRel dlListingContentFieldRel = findDlContentFieldRelationship(dlContentField, existingDlListingContentFieldRels);

                updateDlContentFieldRelationship(dlListingForSaving, dlListingFieldDTO, dlContentField, dlListingContentFieldRel);
            }
        }
    }

    private void setFeaturedAttachment(DlListingDTO dlListingDTO, DlListing dlListingForSaving) {
        if (dlListingDTO.getFeaturedAttachment() != null) {
            dlListingForSaving.setFeaturedAttachment(dlAttachmentRepository.findOne(dlListingDTO.getFeaturedAttachment().getId()));
        }
    }

    private void updateDlContentFieldRelationship(DlListing dlListingForSaving, DlListingFieldDTO dlListingFieldDTO, DlContentField dlContentField, DlListingContentFieldRel dlListingContentFieldRel) {
        if (dlListingContentFieldRel != null) {
            // update existing relationship object
            setContentFieldRelationValue(dlListingFieldDTO, dlContentField, dlListingContentFieldRel);
        } else {
            // create new relationship object
            DlListingContentFieldRel newDlListingContentFieldRel = new DlListingContentFieldRel();
            newDlListingContentFieldRel.setDlContentField(dlContentField);
            newDlListingContentFieldRel.setDlListing(dlListingForSaving);
            setContentFieldRelationValue(dlListingFieldDTO, dlContentField, newDlListingContentFieldRel);
            dlListingForSaving.addDlContentFieldRelationships(newDlListingContentFieldRel);
        }
    }

    private void setContentFieldRelationValue(DlListingFieldDTO dlListingFieldDTO, DlContentField dlContentField, DlListingContentFieldRel newDlListingContentFieldRel) {
        if (dlContentField.getType().equals(DlContentField.Type.CHECKBOX)) {
            // make relation with the selection items
            if (!StringUtils.isEmpty(dlListingFieldDTO.getSelectedValue())) {
                List<Long> idsAsList = getSelectedValuesFromCheckbox(dlListingFieldDTO);
                if (!idsAsList.isEmpty()) {
                    Set<DlContentFieldItem> byIdIn = dlContentFieldItemRepository.findByIdInOrderByOrderNum(idsAsList);
                    newDlListingContentFieldRel.setDlContentFieldItems(byIdIn);
                } else {
                    newDlListingContentFieldRel.setDlContentFieldItems(new HashSet<>());
                }
            }
        } else if (dlContentField.getType().equals(DlContentField.Type.SELECT)
                || dlContentField.getType().equals(DlContentField.Type.DEPENDENT_SELECT)) {
            setSelection(dlListingFieldDTO, newDlListingContentFieldRel);
        } else if (dlContentField.getType().equals(DlContentField.Type.NUMBER_UNIT)) {
            setSelection(dlListingFieldDTO, newDlListingContentFieldRel);
            newDlListingContentFieldRel.setValue(dlListingFieldDTO.getValue());
        } else {
            // set value
            newDlListingContentFieldRel.setValue(dlListingFieldDTO.getValue());
        }
    }

    private List<Long> getSelectedValuesFromCheckbox(DlListingFieldDTO dlListingFieldDTO) {
        try {
            return Arrays.asList(new ObjectMapper().readValue(dlListingFieldDTO.getSelectedValue(), Long[].class));
        } catch (IOException e) {
            log.error("Could not parse dlListingFieldDTO selected value: {}, with name: {} ", dlListingFieldDTO.getSelectedValue(), dlListingFieldDTO.getName(), e);
        }
        return new ArrayList<>();
    }

    private void setSelection(DlListingFieldDTO dlListingFieldDTO, DlListingContentFieldRel newDlListingContentFieldRel) {
        if (!StringUtils.isEmpty(dlListingFieldDTO.getSelectedValue()) && !dlListingFieldDTO.getSelectedValue().equals("-1")) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItemRepository.findOne(Long.valueOf(dlListingFieldDTO.getSelectedValue()));
            Set<DlContentFieldItem> dlContentFieldItemSet = new HashSet<>();
            dlContentFieldItemSet.add(dlContentFieldItem);
            newDlListingContentFieldRel.setDlContentFieldItems(dlContentFieldItemSet);
        }
    }

    private DlListingContentFieldRel findDlContentFieldRelationship(DlContentField dlContentField, Set<DlListingContentFieldRel> dlListingContentFieldRels) {
        if (dlListingContentFieldRels != null) {
            for (DlListingContentFieldRel dlListingContentFieldRel : dlListingContentFieldRels) {
                if (dlContentField.getId().equals(dlListingContentFieldRel.getDlContentField().getId())) {
                    return dlListingContentFieldRel;
                }
            }
        }

        return null;
    }

    private void setLocation(DlListingDTO dlListingDTO, DlListing dlListingForSaving) {
        // set location
        if (dlListingDTO.getDlLocations() != null && !dlListingDTO.getDlLocations().isEmpty()) {
            DlLocationDTO dlLocationDTO = dlListingDTO.getDlLocations().get(0);
            if (dlLocationDTO.getId() != -1L) {
                DlLocation dlLocation = dlLocationRepository.findOne(dlLocationDTO.getId());
                Set<DlListingLocationRel> dlListingLocationRels = dlListingForSaving.getDlListingLocationRels();
                if (dlListingLocationRels != null && !dlListingLocationRels.isEmpty()) {
                    // update
                    DlListingLocationRel dlListingLocationRel = dlListingLocationRels.iterator().next();
                    dlListingLocationRel.setDlLocation(dlLocation);
                } else {
                    // create new
                    DlListingLocationRel dlListingLocationRel = new DlListingLocationRel();
                    dlListingLocationRel.setDlLocation(dlLocation);
                    dlListingLocationRel.setDlListing(dlListingForSaving);
                    dlListingForSaving.addDlLocationRelationship(dlListingLocationRel);
                }
            }
        }
    }

    private void setCategory(DlListingDTO dlListingDTO, DlListing dlListingForSaving) {
        // set category
        if (dlListingDTO.getDlCategories() != null && !dlListingDTO.getDlCategories().isEmpty()) {
            DlCategoryDTO dlCategoryDTO = dlListingDTO.getDlCategories().get(0);
            DlCategory dlCategory = dlCategoryRepository.findOne(dlCategoryDTO.getId());
            Set<DlCategory> dlCategories = new HashSet<>();
            dlCategories.add(dlCategory);
            dlListingForSaving.setDlCategories(dlCategories);
        }
    }


    public Page<DlListingDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all DlListingDTO");
        String languageCode = allRequestParams.get("languageCode");
        Page<DlListing> result = dlListingRepository.findAllByTranslation_languageCode(pageable, languageCode);
        return result.map((DlListing dlListing) -> dlListingMapper.dlListingToDlListingDTO(dlListing, languageCode));
    }

    @Transactional(readOnly = true)
    public DlListingDTO findOne(Long id, String languageCode) {
        log.debug("Request to get DlListingDTO: {}", id);
        long start = System.currentTimeMillis();
        DlListing result = dlListingRepository.findOne(id);
        DlListingDTO dlListingDTO = result != null ? dlListingMapper.dlListingToDlListingDTO(result, languageCode) : null;
        log.info("finOne id: {}, took: {} ms", id, System.currentTimeMillis() - start);
        return dlListingDTO;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlCategoryDTO : {}", id);
        dlListingRepository.delete(id);
        dlListingSearchRepository.delete(id);
    }

    public Page<DlListingDTO> search(DlListingSearchFilter query, Pageable pageable) {
        log.debug("Request to search for a page of Books for query {}", query);

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(query.getText())) {
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(query.getText())
                    .field("title^3")
                    .field("description")
                    .fuzziness(Fuzziness.AUTO));
        }

        boolQueryBuilder
                .must(QueryBuilders.termQuery("status", DlListing.Status.PUBLISHED.toString().toLowerCase()))
                .should(QueryBuilders.termQuery("languageCode", query.getLanguageCode()));

        if (hasSelection(query.getCategoryId())) {
            setLocationFilter(boolQueryBuilder, "translatedCategories", "translatedCategories.id", query.getCategoryId());
        }

        if (hasSelection(query.getCityId())) {
            setLocationFilter(boolQueryBuilder, TRANSLATED_LOCATIONS, TRANSLATED_LOCATIONS_ID, query.getCityId());
        } else if (hasSelection(query.getStateId())) {
            setLocationFilter(boolQueryBuilder, TRANSLATED_LOCATIONS, TRANSLATED_LOCATIONS_ID, query.getStateId());
        } else if (hasSelection(query.getCountryId())) {
            setLocationFilter(boolQueryBuilder, TRANSLATED_LOCATIONS, TRANSLATED_LOCATIONS_ID, query.getCountryId());
        }

        List<DlContentFieldFilter> dlContentFields = query.getContentFields();
        if (!CollectionUtils.isEmpty(dlContentFields)) {
            for (DlContentFieldFilter dlContentField : dlContentFields) {
                if (!StringUtils.isEmpty(dlContentField.getValue())) {
                    boolQueryBuilder.must(QueryBuilders.nestedQuery("dlListingFields",
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.termQuery("dlListingFields.id", dlContentField.getId()))
                                    .must(QueryBuilders.termQuery("dlListingFields.value", dlContentField.getValue()))));
                }

                if (!StringUtils.isEmpty(dlContentField.getSelectedValue())) {
                    boolQueryBuilder.must(QueryBuilders.nestedQuery("dlListingFields",
                            QueryBuilders.boolQuery()
                                    .must(QueryBuilders.termQuery("dlListingFields.id", dlContentField.getId()))
                                    .must(QueryBuilders.termQuery("dlListingFields.selectedValue", dlContentField.getSelectedValue()))));
                }
            }
        }

        return dlListingSearchRepository.search(boolQueryBuilder, pageable);
    }

    private void setLocationFilter(BoolQueryBuilder boolQueryBuilder, String translatedLocations, String s, String locationId) {
        boolQueryBuilder.must(QueryBuilders.nestedQuery(translatedLocations,
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery(s, locationId))));
    }

    private boolean hasSelection(String value) {
        return !StringUtils.isEmpty(value) && !value.equals("-1");
    }

    public DlListingDTO deleteDlListingAttachment(Long id, Long attachmentId) {
        log.debug("Request to delete attachment with id : {}, from DlCategoryDTO : {}", attachmentId, id);
        DlListing dlListing = dlListingRepository.findOne(id);
        DlAttachment attachment = dlListing.removeAttachment(attachmentId);
        dlListing.setModified(new Timestamp(clock.millis()));
        DlListing result = dlListingRepository.save(dlListing);

        if (attachment != null) {
            dlAttachmentRepository.delete(attachment);
            List<String> filePaths = AttachmentUtil.getFilePaths(attachment);
            storageService.delete(filePaths);
        }

        return dlListingMapper.dlListingToDlListingDTO(result, null);
    }

    public void validateForPublishing(DlListingDTO dlListingDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<DlListingDTO>> validate = validator.validate(dlListingDTO);
        if (!validate.isEmpty()) {
            // return validation object and status 400
            log.info("DlListingDTO with id: {}, not valid", dlListingDTO.getId());
        }
    }

    public List<AttachmentDTO> uploadFile(Map<String, MultipartFile> fileMap, Long id) throws IOException {
        DlListing dlListing = dlListingRepository.findOne(id);

        List<AttachmentDTO> uploadedAttachments = new ArrayList<>();

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin);
        if (oneByLogin.isPresent()) {
            for (MultipartFile file : fileMap.values()) {
                AttachmentDTO attachmentDto = storageService.store(file);
                DlAttachment dlAttachment = attachmentMapper.attachmentDTOToAttachment(attachmentDto);
                dlAttachment.setDlListing(dlListing);
                dlAttachmentRepository.save(dlAttachment);
                dlListing.addDlAttachment(dlAttachment);
                uploadedAttachments.add(attachmentMapper.attachmentToAttachmentDTO(dlAttachment));
            }

            dlListing.setStatus(DlListing.Status.DRAFT);
            dlListingRepository.save(dlListing);
            return uploadedAttachments;
        } else {
            throw new UsernameNotFoundException(String.format(USER_S_WAS_NOT_FOUND_IN_THE_DATABASE, currentUserLogin));
        }
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");
        return languageService.findAllActiveLanguages(dlListingRepository);
    }

    public Page<DlListingDTO> findAllByLanguageAndUser(Pageable pageable, String languageCode, User user) {
        Page<DlListing> result = dlListingRepository.findAllByTranslation_languageCodeAndUser(pageable, languageCode, user);
        return result.map((DlListing dlListing) -> dlListingMapper.dlListingToDlListingDTO(dlListing, languageCode));
    }

    public Page<DlListingDTO> findAllForFrontPage(Pageable pageable, String language) {
        Page<DlListing> result = dlListingRepository.findAllForFrontPage(language, pageable);
        return result.map((DlListing dlListing) -> dlListingMapper.dlListingToDlListingDTO(dlListing, language));
    }
}
