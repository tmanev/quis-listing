package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.StaticPage;
import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.StaticPageRepository;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.service.post.dto.StaticPageDTO;
import com.manev.quislisting.service.post.exception.PostDifferentLanguageException;
import com.manev.quislisting.service.post.exception.PostNotFoundException;
import com.manev.quislisting.service.post.mapper.QlStaticPageMapper;
import com.manev.quislisting.service.qlml.LanguageService;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class StaticPageService {

    private final Logger log = LoggerFactory.getLogger(StaticPageService.class);

    private StaticPageRepository staticPageRepository;
    private LanguageRepository languageRepository;
    private TranslationGroupRepository translationGroupRepository;
    private QlStaticPageMapper qlStaticPageMapper;
    private LanguageService languageService;

    public StaticPageService(StaticPageRepository staticPageRepository, LanguageRepository languageRepository,
                             TranslationGroupRepository translationGroupRepository, QlStaticPageMapper qlStaticPageMapper,
                             LanguageService languageService) {
        this.staticPageRepository = staticPageRepository;
        this.languageRepository = languageRepository;
        this.translationGroupRepository = translationGroupRepository;
        this.qlStaticPageMapper = qlStaticPageMapper;
        this.languageService = languageService;
    }

    public StaticPageDTO save(StaticPageDTO staticPageDTO) {
        log.debug("Request to save StaticPageDTO : {}", staticPageDTO);

        TranslationGroup translationGroup;
        if (staticPageDTO.getTranslationGroupId() != null) {
            translationGroup = translationGroupRepository.findOne(staticPageDTO.getTranslationGroupId());
        } else {
            translationGroup = new TranslationGroup();
        }

        StaticPage page;
        if (staticPageDTO.getId() == null) {
            page = qlStaticPageMapper.pageDTOtoPage(staticPageDTO);
            page.setTranslation(
                    TranslationBuilder.aTranslation()
                            .withLanguageCode(staticPageDTO.getLanguageCode())
                            .withTranslationGroup(translationGroup)
                            .withSourceLanguageCode(staticPageDTO.getSourceLanguageCode())
                            .build());
        } else {
            StaticPage existingQlPage = staticPageRepository.findOne(staticPageDTO.getId());
            page = qlStaticPageMapper.pageDTOtoPage(existingQlPage, staticPageDTO);
        }

        page = staticPageRepository.save(page);
        List<Language> allByActive = languageRepository.findAllByActive(true);
        return qlStaticPageMapper.staticPageToStaticPageDTO(page, allByActive);
    }

    public Page<StaticPageDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all StaticPageDTO");
        String languageCode = allRequestParams.get("languageCode");
        List<Language> allByActive = languageRepository.findAllByActive(true);
        Page<StaticPage> result = staticPageRepository.findAllByTranslation_languageCode(pageable, languageCode);
        return result.map(qlPage -> qlStaticPageMapper.staticPageToStaticPageDTO(qlPage, allByActive));
    }

    @Transactional(readOnly = true)
    public StaticPageDTO findOne(Long id) {
        log.debug("Request to get StaticPageDTO: {}", id);
        StaticPage result = staticPageRepository.findOne(id);
        List<Language> allByActive = languageRepository.findAllByActive(true);
        return result != null ? qlStaticPageMapper.staticPageToStaticPageDTO(result, allByActive) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete StaticPageDTO : {}", id);
        staticPageRepository.delete(id);
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");
        return languageService.findAllActiveLanguages(staticPageRepository);
    }

    public StaticPage retrievePost(String languageCode, String postId) {
        StaticPage post = staticPageRepository.findOne(Long.valueOf(postId));
        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(languageCode)) {
            Translation translationForLanguage = translationExists(languageCode, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                post = staticPageRepository.findOneByTranslation(translationForLanguage);
            }
        }

        return post;
    }

    private Translation translationExists(String languageCode, Set<Translation> translationList) {
        for (Translation translation : translationList) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation;
            }
        }
        return null;
    }

    public StaticPage findOneByName(String name) {
        return staticPageRepository.findOneByName(name);
    }

    public StaticPage findOneByName(String name, String language) throws PostNotFoundException, PostDifferentLanguageException {
        StaticPage post = staticPageRepository.findOneByName(name);

        if (post == null) {
            throw new PostNotFoundException(String.format("Post with name: %s not found", name));
        }

        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(language)) {
            // find post related to the translation
            // and redirect it to the page url

            // check if there is a translation
            Translation translationForLanguage = translationExists(language, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                StaticPage translatedPost = staticPageRepository.findOneByTranslation(translationForLanguage);
                throw new PostDifferentLanguageException((translatedPost.getName()));
            }
        }

        return post;
    }
}
