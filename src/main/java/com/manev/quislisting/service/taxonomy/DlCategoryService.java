package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.model.CategoryCount;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class DlCategoryService {

    private final Logger log = LoggerFactory.getLogger(DlCategoryService.class);

    private DlCategoryRepository dlCategoryRepository;
    private LanguageRepository languageRepository;

    private TranslationGroupRepository translationGroupRepository;

    private DlCategoryMapper dlCategoryMapper;
    private ActiveLanguageMapper activeLanguageMapper;

    public DlCategoryService(DlCategoryRepository dlCategoryRepository, DlCategoryMapper dlCategoryMapper,
                             TranslationGroupRepository translationGroupRepository,
                             LanguageRepository languageRepository,
                             ActiveLanguageMapper activeLanguageMapper) {
        this.dlCategoryRepository = dlCategoryRepository;
        this.dlCategoryMapper = dlCategoryMapper;
        this.translationGroupRepository = translationGroupRepository;
        this.languageRepository = languageRepository;
        this.activeLanguageMapper = activeLanguageMapper;
    }

    public DlCategoryDTO save(DlCategoryDTO dlCategoryDTO) {
        log.debug("Request to save DlCategoryDTO : {}", dlCategoryDTO);

        DlCategory dlCategory;
        if (dlCategoryDTO.getId() != null) {
            DlCategory existingDlCategory = dlCategoryRepository.findOne(dlCategoryDTO.getId());
            dlCategory = dlCategoryMapper.dlCategoryDtoToDlCategory(existingDlCategory, dlCategoryDTO);
        } else {
            dlCategory = dlCategoryMapper.dlCategoryDtoToDlCategory(dlCategoryDTO);
        }

        setTranslation(dlCategoryDTO, dlCategory);

        if (dlCategoryDTO.getParentId() != null) {
            dlCategory.setParent(dlCategoryRepository.findOne(dlCategoryDTO.getParentId()));
        }
        dlCategory = dlCategoryRepository.save(dlCategory);
        return dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory);
    }

    private void setTranslation(DlCategoryDTO dlCategoryDTO, DlCategory dlCategory) {
        if (dlCategory.getTranslation() == null) {
            if (dlCategoryDTO.getTranslationGroupId() != null) {
                // this is translation use existing translation group
                TranslationGroup translationGroup = translationGroupRepository.findOne(dlCategoryDTO.getTranslationGroupId());
                dlCategory.setTranslation(
                        createTranslation(dlCategoryDTO.getLanguageCode(), translationGroup, dlCategoryDTO.getSourceLanguageCode()));
            } else {
                // create new translation group
                dlCategory.setTranslation(
                        createTranslation(dlCategoryDTO.getLanguageCode(), new TranslationGroup(), dlCategoryDTO.getSourceLanguageCode()));
            }
        }
    }

    private Translation createTranslation(String languageCode, TranslationGroup translationGroup, String sourceLanguageCode) {
        return TranslationBuilder.aTranslation()
                .withLanguageCode(languageCode)
                .withTranslationGroup(translationGroup)
                .withSourceLanguageCode(sourceLanguageCode)
                .build();
    }

    public Page<DlCategoryDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all DlCategoryDTO");
        String languageCode = allRequestParams.get("languageCode");
        Page<DlCategory> result = dlCategoryRepository.findAllByTranslation_languageCode(pageable, languageCode);
        List<DlCategoryDTO> dlCategoryDTOs = dlCategoryMapper.dlCategoryToDlCategoryDtoFlat(result.getContent());
        return new PageImpl<>(dlCategoryDTOs, pageable, result.getTotalElements());
    }

    public List<DlCategoryDTO> findAllByLanguageCode(String languageCode) {
        List<DlCategory> result = dlCategoryRepository.findAllByTranslation_languageCode(languageCode);
        return dlCategoryMapper.dlCategoryToDlCategoryDtoFlat(result);
    }

    public Map<DlCategory, List<DlCategory>> findAllByLanguageCodeGrouped(String languageCode) {
        Map<DlCategory, List<DlCategory>> result = new HashMap<>();
        List<DlCategory> dlCategories = dlCategoryRepository.findAllByTranslation_languageCode(languageCode);
        for (DlCategory dlCategory : dlCategories) {
            if (dlCategory.getParent() != null) {
                List<DlCategory> children = result.get(dlCategory.getParent());
                if (children == null) {
                    ArrayList<DlCategory> emptyChildrenList = new ArrayList<>();
                    emptyChildrenList.add(dlCategory);
                    result.put(dlCategory.getParent(), emptyChildrenList);
                } else {
                    children.add(dlCategory);
                }
            } else {
                result.computeIfAbsent(dlCategory, k -> new ArrayList<>());
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    public DlCategoryDTO findOne(Long id) {
        log.debug("Request to get DlCategoryDTO : {}", id);
        DlCategory result = dlCategoryRepository.findOne(id);
        return result != null ? dlCategoryMapper.dlCategoryToDlCategoryDTO(result) : null;
    }

    @Transactional(readOnly = true)
    public DlCategoryDTO findOneByTranslationId(Long id) {
        DlCategory result = dlCategoryRepository.findByTranslation_id(id);
        return result != null ? dlCategoryMapper.dlCategoryToDlCategoryDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlCategoryDTO : {}", id);
        dlCategoryRepository.delete(id);
    }

    public List<CategoryCount> findAllCategoriesWithCount(String languageCode) {
        return dlCategoryRepository.findCategoriesWithCount();
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");
        List<ActiveLanguageDTO> result = new ArrayList<>();

        List<Language> allByActive = languageRepository.findAllByActive(true);
        for (Language language : allByActive) {
            Long count = dlCategoryRepository.countByTranslation_languageCode(language.getCode());
            result.add(activeLanguageMapper.toActiveLanguageDTO(language, count));
        }

        return result;
    }

    public void bindDlCategories(Long sourceId, Long targetId) {
        DlCategory source = dlCategoryRepository.findOne(sourceId);
        TranslationGroup sourceTranslationGroup = source.getTranslation().getTranslationGroup();

        DlCategory target = dlCategoryRepository.findOne(targetId);
        Translation targetTranslation = target.getTranslation();
        TranslationGroup targetTranslationGroup = targetTranslation.getTranslationGroup();
        targetTranslation.setTranslationGroup(sourceTranslationGroup);

        dlCategoryRepository.save(target);
        translationGroupRepository.delete(targetTranslationGroup);
    }
}
