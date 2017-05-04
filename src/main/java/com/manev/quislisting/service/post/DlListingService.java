package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingField;
import com.manev.quislisting.service.post.mapper.AttachmentMapper;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.storage.StorageService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import com.manev.quislisting.service.util.SlugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;


@Service
@Transactional
public class DlListingService {

    private final Logger log = LoggerFactory.getLogger(DlListingService.class);

    private DlListingRepository dlListingRepository;
    private UserRepository userRepository;
    private LanguageRepository languageRepository;
    private DlCategoryRepository dlCategoryRepository;
    private DlLocationRepository dlLocationRepository;

    private DlListingMapper dlListingMapper;
    private ActiveLanguageMapper activeLanguageMapper;
    private AttachmentMapper attachmentMapper;
    
    private StorageService storageService;

    public DlListingService(DlListingRepository dlListingRepository, UserRepository userRepository, LanguageRepository languageRepository, DlCategoryRepository dlCategoryRepository, DlLocationRepository dlLocationRepository, DlListingMapper dlListingMapper, ActiveLanguageMapper activeLanguageMapper, AttachmentMapper attachmentMapper, StorageService storageService) {
        this.dlListingRepository = dlListingRepository;
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
        this.dlCategoryRepository = dlCategoryRepository;
        this.dlLocationRepository = dlLocationRepository;
        this.dlListingMapper = dlListingMapper;
        this.activeLanguageMapper = activeLanguageMapper;
        this.attachmentMapper = attachmentMapper;
        this.storageService = storageService;
    }

    public DlListingDTO save(DlListingDTO dlListingDTO) {
        log.debug("Request to save DlListingDTO : {}", dlListingDTO);
        ZonedDateTime now = ZonedDateTime.now();

        DlListing dlListingForSaving;
        if (dlListingDTO.getId() != null) {
            dlListingForSaving = dlListingRepository.findOne(dlListingDTO.getId());

            setCommonProperties(dlListingDTO, now, dlListingForSaving);
        } else {
            Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
            dlListingForSaving = new DlListing();

            setCommonProperties(dlListingDTO, now, dlListingForSaving);

            dlListingForSaving.setStatus(DlListing.Status.UNFINISHED);
            dlListingForSaving.setTranslation(
                    TranslationBuilder.aTranslation()
                            .withLanguageCode(dlListingDTO.getLanguageCode())
                            .withTranslationGroup(new TranslationGroup())
                            .build()
            );

            dlListingForSaving.setCreated(now);
            dlListingForSaving.setUser(oneByLogin.get());
        }

        DlListing savedDlListing = dlListingRepository.save(dlListingForSaving);
        return dlListingMapper.dlListingToDlListingDTO(savedDlListing);
    }

    private void setCommonProperties(DlListingDTO dlListingDTO, ZonedDateTime now, DlListing dlListingForSaving) {
        dlListingForSaving.setTitle(dlListingDTO.getTitle());
        dlListingForSaving.setName(SlugUtil.getFileNameSlug(dlListingDTO.getTitle()));
        dlListingForSaving.setContent(dlListingDTO.getContent());
        dlListingForSaving.setModified(now);

        setCategory(dlListingDTO, dlListingForSaving);
        setLocation(dlListingDTO, dlListingForSaving);

        List<DlListingField> dlListingFields = dlListingDTO.getDlListingFields();
        if (dlListingFields != null) {
            for (DlListingField dlListingField : dlListingFields) {
                dlListingForSaving.addPostMeta(new PostMeta(dlListingForSaving, dlListingField.getFieldId(),
                        dlListingField.getValue()));
            }
        }
    }

    private void setLocation(DlListingDTO dlListingDTO, DlListing dlListingForSaving) {
        // set location
        if (dlListingDTO.getDlLocations() != null && !dlListingDTO.getDlLocations().isEmpty()) {
            DlLocationDTO dlLocationDTO = dlListingDTO.getDlLocations().get(0);
            DlLocation dlLocation = dlLocationRepository.findOne(dlLocationDTO.getId());
            Set<DlLocation> dlLocations = new HashSet<>();
            dlLocations.add(dlLocation);
            dlListingForSaving.setDlLocations(dlLocations);
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
        return result.map(dlListingMapper::dlListingToDlListingDTO);
    }

    @Transactional(readOnly = true)
    public DlListingDTO findOne(Long id) {
        log.debug("Request to get DlListingDTO: {}", id);
        DlListing result = dlListingRepository.findOne(id);
        return result != null ? dlListingMapper.dlListingToDlListingDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlCategoryDTO : {}", id);
        dlListingRepository.delete(id);
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");

        List<ActiveLanguageDTO> result = new ArrayList<>();

        List<Language> allByActive = languageRepository.findAllByActive(true);
        for (Language language : allByActive) {
            Long count = dlListingRepository.countByTranslation_languageCode(language.getCode());
            result.add(activeLanguageMapper.toActiveLanguageDTO(language, count));
        }

        return result;
    }

    public boolean publish(DlListingDTO dlListingDTO) {
        DlListing dlListing = dlListingRepository.findOne(dlListingDTO.getId());
        dlListing.setStatus(DlListing.Status.PUBLISH);
        dlListingRepository.save(dlListing);
        return true;
    }

    public DlListingDTO uploadFile(MultipartFile[] files, Long id) throws IOException, RepositoryException {
        DlListing dlListing = dlListingRepository.findOne(id);

        for (MultipartFile file : files) {
            AttachmentDTO attachmentDto = storageService.store(file);
            Attachment attachment = attachmentMapper.attachmentDTOToAttachment(attachmentDto);
            attachment.setStatus(Attachment.Status.LISTING);
            Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
            attachment.setUser(oneByLogin.get());

            dlListing.addAttachment(attachment);
        }

        DlListing result = dlListingRepository.save(dlListing);
        return dlListingMapper.dlListingToDlListingDTO(result);
    }
}
