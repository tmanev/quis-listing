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
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.search.DlListingSearchRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.EmailSendingService;
import com.manev.quislisting.service.dto.ApproveDTO;
import com.manev.quislisting.service.form.DlCategoryForm;
import com.manev.quislisting.service.form.DlFeaturedAttachmentForm;
import com.manev.quislisting.service.form.DlListingFieldForm;
import com.manev.quislisting.service.form.DlListingForm;
import com.manev.quislisting.service.form.DlLocationForm;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.QlImageFile;
import com.manev.quislisting.service.post.mapper.AttachmentMapper;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.qlml.LanguageService;
import com.manev.quislisting.service.storage.StorageService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
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

import javax.imageio.ImageIO;
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
    private static final String TRANSLATED_LOCATIONS = "translatedLocations";
    private static final String TRANSLATED_LOCATIONS_ID = "translatedLocations.id";
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

    public DlListingService(DlListingRepository dlListingRepository, UserRepository userRepository,
                            DlCategoryRepository dlCategoryRepository, DlLocationRepository dlLocationRepository,
                            DlListingMapper dlListingMapper, AttachmentMapper attachmentMapper,
                            StorageService storageService, LanguageService languageService,
                            DlContentFieldRepository dlContentFieldRepository,
                            DlContentFieldItemRepository dlContentFieldItemRepository,
                            DlAttachmentRepository dlAttachmentRepository, DlListingSearchRepository dlListingSearchRepository, EmailSendingService emailSendingService) {
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
    }

    public DlListingDTO create(DlListingForm dlListingForm, User user, String languageCode) {
        Timestamp now = new Timestamp(clock.millis());

        DlListing dlListingForSaving = new DlListing();

        setAllProperties(dlListingForm, dlListingForSaving);

        TranslationGroup translationGroup = new TranslationGroup();
        dlListingForSaving.setTranslation(
                TranslationBuilder.aTranslation()
                        .withLanguageCode(languageCode)
                        .withTranslationGroup(translationGroup)
                        .build());

        dlListingForSaving.setCreated(now);
        dlListingForSaving.setModified(now);
        dlListingForSaving.setUser(user);
        dlListingForSaving.setStatus(DlListing.Status.DRAFT);

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO updateDescription(DlListingForm form) {
        DlListing dlListingForSaving = dlListingRepository.findOne(form.getId());

        setDescription(dlListingForSaving, form.getTitle(), form.getContent());
        setCategory(form.getDlCategories(), dlListingForSaving);
        dlListingForSaving.setModified(new Timestamp(clock.millis()));

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO updateDetails(DlListingForm form) {
        DlListing dlListingForSaving = dlListingRepository.findOne(form.getId());

        setDlListingFields(dlListingForSaving, form.getDlListingFields());
        dlListingForSaving.setModified(new Timestamp(clock.millis()));

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO updateLocation(DlListingForm form) {
        DlListing dlListingForSaving = dlListingRepository.findOne(form.getId());

        setLocation(form.getDlLocations(), dlListingForSaving);
        dlListingForSaving.setModified(new Timestamp(clock.millis()));

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO updateFeaturedAttachment(DlListingForm form) {
        DlListing dlListingForSaving = dlListingRepository.findOne(form.getId());

        setFeaturedAttachment(form.getFeaturedAttachment(), dlListingForSaving);
        dlListingForSaving.setModified(new Timestamp(clock.millis()));

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO updateStatus(DlListingForm form) {
        DlListing dlListingForSaving = dlListingRepository.findOne(form.getId());
        dlListingForSaving.setModified(new Timestamp(clock.millis()));

        dlListingForSaving.setStatus(form.getStatus());

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO update(DlListingForm dlListingForm) {
        DlListing dlListingForSaving = dlListingRepository.findOne(dlListingForm.getId());

        setAllProperties(dlListingForm, dlListingForSaving);
        dlListingForSaving.setModified(new Timestamp(clock.millis()));

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);

        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        dlListingSearchRepository.save(savedDlListingDTO);
        return savedDlListingDTO;
    }

    public DlListingDTO saveAndPublish(DlListingForm dlListingForm) {
        log.debug("Request to save DlListingDTO : {}", dlListingForm);

        DlListing dlListingForSaving = dlListingRepository.findOne(dlListingForm.getId());

        setAllProperties(dlListingForm, dlListingForSaving);
        dlListingForSaving.setModified(new Timestamp(clock.millis()));
        dlListingForSaving.setStatus(DlListing.Status.PUBLISHED);

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);
        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(dlListingForSaving);
        dlListingSearchRepository.save(savedDlListingDTO);

        emailSendingService.sendPublishedNotification(savedDlListing);
        // send to user that publication is published
        return savedDlListingDTO;
    }

    public DlListingDTO publishListing(Long id) {
        DlListing dlListingForSaving = dlListingRepository.findOne(id);
        dlListingForSaving.setStatus(DlListing.Status.PUBLISHED);
        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);
        DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(savedDlListing);
        return dlListingSearchRepository.save(savedDlListingDTO);
    }

    public DlListingDTO approveListing(Long id) {
        DlListing dlListing = dlListingRepository.findOne(id);

        dlListing.setApproved(Boolean.TRUE);
        dlListing.setApprovedModified(new Timestamp(clock.millis()));
        DlListing save = dlListingRepository.save(dlListing);

        DlListingDTO dlListingDTO = dlListingMapper.dlListingToDlListingDTO(save);
        dlListingSearchRepository.save(dlListingDTO);

        return dlListingDTO;
    }

    public DlListingDTO disapproveListing(Long id, ApproveDTO approveDTO) {
        DlListing dlListing = dlListingRepository.findOne(id);

        dlListing.setApproved(Boolean.FALSE);
        dlListing.setApprovedModified(new Timestamp(clock.millis()));
        dlListing.setStatus(DlListing.Status.PUBLISH_DISAPPROVED);
        DlListing save = dlListingRepository.save(dlListing);

        DlListingDTO dlListingDTO = dlListingMapper.dlListingToDlListingDTO(save);
        dlListingSearchRepository.save(dlListingDTO);

        emailSendingService.sendListingDisapprovedEmail(save, approveDTO.getMessage());

        return dlListingDTO;
    }

    private void setDescription(DlListing dlListing, String title, String content) {
        dlListing.setTitle(title);
        dlListing.setName(SlugUtil.getFileNameSlug(title));
        dlListing.setContent(content);
    }

    private void setAllProperties(DlListingForm dlListingForm, DlListing dlListingForSaving) {
        setDescription(dlListingForSaving, dlListingForm.getTitle(), dlListingForm.getContent());
        setCategory(dlListingForm.getDlCategories(), dlListingForSaving);
        setDlListingFields(dlListingForSaving, dlListingForm.getDlListingFields());
        setLocation(dlListingForm.getDlLocations(), dlListingForSaving);
        setFeaturedAttachment(dlListingForm.getFeaturedAttachment(), dlListingForSaving);

        dlListingForSaving.setApproved(null);
        dlListingForSaving.setApprovedModified(null);
    }

    private void setDlListingFields(DlListing dlListingForSaving, List<DlListingFieldForm> dlListingFieldDTOS) {
        if (!CollectionUtils.isEmpty(dlListingFieldDTOS)) {
            Set<DlListingContentFieldRel> existingDlListingContentFieldRels = dlListingForSaving.getDlListingContentFieldRels();
            for (DlListingFieldForm dlListingFieldForm : dlListingFieldDTOS) {
                DlContentField dlContentField = dlContentFieldRepository.findOne(dlListingFieldForm.getId());
                DlListingContentFieldRel dlListingContentFieldRel = findDlContentFieldRelationship(dlContentField, existingDlListingContentFieldRels);

                updateDlContentFieldRelationship(dlListingForSaving, dlListingFieldForm, dlContentField, dlListingContentFieldRel);
            }
        }
    }

    private void setFeaturedAttachment(DlFeaturedAttachmentForm form, DlListing dlListingForSaving) {
        if (form != null) {
            dlListingForSaving.setFeaturedAttachment(dlAttachmentRepository.findOne(form.getId()));
        }
    }

    private void updateDlContentFieldRelationship(DlListing dlListingForSaving, DlListingFieldForm dlListingFieldForm, DlContentField dlContentField, DlListingContentFieldRel dlListingContentFieldRel) {
        if (dlListingContentFieldRel != null) {
            // update existing relationship object
            setContentFieldRelationValue(dlListingFieldForm, dlContentField, dlListingContentFieldRel);
        } else {
            // create new relationship object
            DlListingContentFieldRel newDlListingContentFieldRel = new DlListingContentFieldRel();
            newDlListingContentFieldRel.setDlContentField(dlContentField);
            newDlListingContentFieldRel.setDlListing(dlListingForSaving);
            setContentFieldRelationValue(dlListingFieldForm, dlContentField, newDlListingContentFieldRel);
            dlListingForSaving.addDlContentFieldRelationships(newDlListingContentFieldRel);
        }
    }

    private void setContentFieldRelationValue(DlListingFieldForm dlListingFieldForm, DlContentField dlContentField, DlListingContentFieldRel newDlListingContentFieldRel) {
        if (dlContentField.getType().equals(DlContentField.Type.CHECKBOX)) {
            // make relation with the selection items
            if (!StringUtils.isEmpty(dlListingFieldForm.getSelectedValue())) {
                List<Long> idsAsList = getSelectedValuesFromCheckbox(dlListingFieldForm);
                if (!idsAsList.isEmpty()) {
                    Set<DlContentFieldItem> byIdIn = dlContentFieldItemRepository.findByIdInOrderByOrderNum(idsAsList);
                    newDlListingContentFieldRel.setDlContentFieldItems(byIdIn);
                } else {
                    newDlListingContentFieldRel.setDlContentFieldItems(new HashSet<>());
                }
            }
        } else if (dlContentField.getType().equals(DlContentField.Type.SELECT)
                || dlContentField.getType().equals(DlContentField.Type.DEPENDENT_SELECT)) {
            setSelection(dlListingFieldForm, newDlListingContentFieldRel);
        } else if (dlContentField.getType().equals(DlContentField.Type.NUMBER_UNIT)) {
            setSelection(dlListingFieldForm, newDlListingContentFieldRel);
            newDlListingContentFieldRel.setValue(dlListingFieldForm.getValue());
        } else {
            // set value
            newDlListingContentFieldRel.setValue(dlListingFieldForm.getValue());
        }
    }

    private List<Long> getSelectedValuesFromCheckbox(DlListingFieldForm dlListingFieldForm) {
        try {
            return Arrays.asList(new ObjectMapper().readValue(dlListingFieldForm.getSelectedValue(), Long[].class));
        } catch (IOException e) {
            log.error("Could not parse dlListingFieldForm selected value: {}, with id: {} ", dlListingFieldForm.getSelectedValue(), dlListingFieldForm.getId(), e);
        }
        return new ArrayList<>();
    }

    private void setSelection(DlListingFieldForm dlListingFieldForm, DlListingContentFieldRel newDlListingContentFieldRel) {
        if (!StringUtils.isEmpty(dlListingFieldForm.getSelectedValue()) && !dlListingFieldForm.getSelectedValue().equals("-1")) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItemRepository.findOne(Long.valueOf(dlListingFieldForm.getSelectedValue()));
            Set<DlContentFieldItem> dlContentFieldItemSet = new HashSet<>();
            dlContentFieldItemSet.add(dlContentFieldItem);
            newDlListingContentFieldRel.setDlContentFieldItems(dlContentFieldItemSet);
        } else {
            newDlListingContentFieldRel.setDlContentFieldItems(new HashSet<>());
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

    private void setLocation(List<DlLocationForm> dlLocationForms, DlListing dlListingForSaving) {
        // set location
        if (!CollectionUtils.isEmpty(dlLocationForms)) {
            DlLocationForm dlLocationForm = dlLocationForms.get(0);
            if (dlLocationForm.getId() != -1L) {
                DlLocation dlLocation = dlLocationRepository.findOne(dlLocationForm.getId());
                Set<DlListingLocationRel> dlListingLocationRels = dlListingForSaving.getDlListingLocationRels();
                if (!CollectionUtils.isEmpty(dlListingLocationRels)) {
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

    private void setCategory(List<DlCategoryForm> dlCategoryForms, DlListing dlListingForSaving) {
        // set category
        if (!CollectionUtils.isEmpty(dlCategoryForms)) {
            DlCategoryForm dlCategoryForm = dlCategoryForms.get(0);
            DlCategory dlCategory = dlCategoryRepository.findOne(dlCategoryForm.getId());
            Set<DlCategory> dlCategories = new HashSet<>();
            dlCategories.add(dlCategory);
            dlListingForSaving.setDlCategories(dlCategories);
        }
    }

    public Page<DlListingDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all DlListingDTO");
        String languageCode = allRequestParams.get("languageCode");
        Page<DlListing> result = dlListingRepository.findAllByTranslation_languageCode(pageable, languageCode);
        return result.map((DlListing dlListing) -> dlListingMapper.dlListingToDlListingDTO(dlListing));
    }

    @Transactional(readOnly = true)
    public DlListingDTO findOne(Long id) {
        log.debug("Request to get DlListingDTO: {}", id);
        long start = System.currentTimeMillis();
        DlListing result = dlListingRepository.findOne(id);
        DlListingDTO dlListingDTO = result != null ? dlListingMapper.dlListingToDlListingDTO(result) : null;
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

        return dlListingMapper.dlListingToDlListingDTO(result);
    }

    public void validateForPublishing(DlListingForm dlListingForm) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<DlListingForm>> validate = validator.validate(dlListingForm);
        if (!validate.isEmpty()) {
            // return validation object and status 400
            log.info("DlListingForm with id: {}, not valid", dlListingForm.getId());
        }
    }

    public List<AttachmentDTO> uploadFile(Map<String, MultipartFile> fileMap, Long id) throws IOException {
        DlListing dlListing = dlListingRepository.findOne(id);

        List<AttachmentDTO> uploadedAttachments = new ArrayList<>();

        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        Optional<User> oneByLogin = userRepository.findOneByLogin(currentUserLogin);
        if (oneByLogin.isPresent()) {
            for (MultipartFile file : fileMap.values()) {
                QlImageFile qlImageFile = getQlImageFile(file);
                AttachmentDTO attachmentDto = storageService.store(qlImageFile);
                DlAttachment dlAttachment = attachmentMapper.attachmentDTOToAttachment(attachmentDto);
                dlAttachment.setDlListing(dlListing);
                dlAttachmentRepository.save(dlAttachment);
                dlListing.addDlAttachment(dlAttachment);
                uploadedAttachments.add(attachmentMapper.attachmentToAttachmentDTO(dlAttachment));
            }

            dlListingRepository.save(dlListing);
            return uploadedAttachments;
        } else {
            throw new UsernameNotFoundException(String.format(USER_S_WAS_NOT_FOUND_IN_THE_DATABASE, currentUserLogin));
        }
    }

    private QlImageFile getQlImageFile(MultipartFile multipartFile) throws IOException {
        QlImageFile qlImageFile = new QlImageFile();
        qlImageFile.setOriginalFilename(multipartFile.getOriginalFilename());
        qlImageFile.setContentType(multipartFile.getContentType());
        qlImageFile.setBufferedImage(ImageIO.read(multipartFile.getInputStream()));
        return qlImageFile;
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");
        return languageService.findAllActiveLanguages(dlListingRepository);
    }

    public Page<DlListingDTO> findAllByLanguageAndUser(Pageable pageable, String languageCode, User user) {
        Page<DlListing> result = dlListingRepository.findAllByTranslation_languageCodeAndUser(pageable, languageCode, user);
        return result.map((DlListing dlListing) -> dlListingMapper.dlListingToDlListingDTO(dlListing));
    }

    public Page<DlListingDTO> findAllForFrontPage(Pageable pageable, String language) {
        Page<DlListing> result = dlListingRepository.findAllForFrontPage(language, pageable);
        return result.map((DlListing dlListing) -> dlListingMapper.dlListingToDlListingDTO(dlListing));
    }
}
