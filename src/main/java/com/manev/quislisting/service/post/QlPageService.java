package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PageRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.post.dto.QlPageDTO;
import com.manev.quislisting.service.post.mapper.QlPageMapper;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import com.manev.quislisting.service.post.mapper.QlPageMapper;
//import com.manev.quislisting.service.post.mapper.QlPageMapper;


@Service
@Transactional
public class QlPageService {

    private final Logger log = LoggerFactory.getLogger(QlPageService.class);

    private PageRepository pageRepository;
    private LanguageRepository languageRepository;
    private UserRepository userRepository;
    private TranslationGroupRepository translationGroupRepository;

    private QlPageMapper qlPageMapper;
    private ActiveLanguageMapper activeLanguageMapper;

    public QlPageService(PageRepository pageRepository, LanguageRepository languageRepository, UserRepository userRepository, TranslationGroupRepository translationGroupRepository, QlPageMapper qlPageMapper, ActiveLanguageMapper activeLanguageMapper) {
        this.pageRepository = pageRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.translationGroupRepository = translationGroupRepository;
        this.qlPageMapper = qlPageMapper;
        this.activeLanguageMapper = activeLanguageMapper;
    }

    public QlPageDTO save(QlPageDTO qlPageDTO) {
        log.debug("Request to save QlPageDTO : {}", qlPageDTO);

        TranslationGroup translationGroup;
        if (qlPageDTO.getTranslationGroupId() != null) {
            translationGroup = translationGroupRepository.findOne(qlPageDTO.getTranslationGroupId());
        } else {
            translationGroup = new TranslationGroup();
        }

        QlPage page;
        if (qlPageDTO.getId() == null) {
            page = qlPageMapper.pageDTOtoPage(qlPageDTO);
            Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
            page.setUser(oneByLogin.get());
            ZonedDateTime currentDateTime = ZonedDateTime.now();
            page.setCreated(currentDateTime);
            page.setModified(currentDateTime);
            page.setTranslation(
                    TranslationBuilder.aTranslation()
                            .withLanguageCode(qlPageDTO.getLanguageCode())
                            .withTranslationGroup(translationGroup)
                            .withSourceLanguageCode(qlPageDTO.getSourceLanguageCode())
                            .build());
        } else {
            QlPage existingQlPage = pageRepository.findOne(qlPageDTO.getId());
            page = qlPageMapper.pageDTOtoPage(existingQlPage, qlPageDTO);
            page.setModified(ZonedDateTime.now());
        }

        page = pageRepository.save(page);
        List<Language> allByActive = languageRepository.findAllByActive(true);
        return qlPageMapper.pageToPageDTO(page, allByActive);
    }

    public Page<QlPageDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all QlPageDTO");
        String languageCode = allRequestParams.get("languageCode");
        List<Language> allByActive = languageRepository.findAllByActive(true);
        Page<QlPage> result = pageRepository.findAllByTranslation_languageCode(pageable, languageCode);
        return result.map(qlPage -> qlPageMapper.pageToPageDTO(qlPage, allByActive));
    }

    @Transactional(readOnly = true)
    public QlPageDTO findOne(Long id) {
        log.debug("Request to get QlPageDTO: {}", id);
        QlPage result = pageRepository.findOne(id);
        List<Language> allByActive = languageRepository.findAllByActive(true);
        return result != null ? qlPageMapper.pageToPageDTO(result, allByActive) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete QlPageDTO : {}", id);
        pageRepository.delete(id);
    }

    public List<ActiveLanguageDTO> findAllActiveLanguages() {
        log.debug("Request to retrieve all active languages");

        List<ActiveLanguageDTO> result = new ArrayList<>();

        List<Language> allByActive = languageRepository.findAllByActive(true);
        for (Language language : allByActive) {
            Long count = pageRepository.countByTranslation_languageCode(language.getCode());
            result.add(activeLanguageMapper.toActiveLanguageDTO(language, count));
        }

        return result;
    }
}
