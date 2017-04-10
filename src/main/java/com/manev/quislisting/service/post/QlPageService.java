package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PageRepository;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.post.dto.QlPageDTO;
//import com.manev.quislisting.service.post.mapper.QlPageMapper;
//import com.manev.quislisting.service.post.mapper.QlPageMapper;
import com.manev.quislisting.service.post.mapper.QlPageMapper;
import com.manev.quislisting.service.taxonomy.dto.ActiveLanguageDTO;
import com.manev.quislisting.service.taxonomy.mapper.ActiveLanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class QlPageService {

    private final Logger log = LoggerFactory.getLogger(QlPageService.class);

    private PageRepository pageRepository;
    private LanguageRepository languageRepository;
    private UserRepository userRepository;

    private QlPageMapper qlPageMapper;
    private ActiveLanguageMapper activeLanguageMapper;

    public QlPageService(PageRepository pageRepository, LanguageRepository languageRepository, UserRepository userRepository, QlPageMapper qlPageMapper, ActiveLanguageMapper activeLanguageMapper) {
        this.pageRepository = pageRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.qlPageMapper = qlPageMapper;
        this.activeLanguageMapper = activeLanguageMapper;
    }

    public QlPageDTO save(QlPageDTO qlPageDTO) {
        log.debug("Request to save QlPageDTO : {}", qlPageDTO);

        QlPage page = qlPageMapper.pageDTOtoPage(qlPageDTO);
        Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        page.setUser(oneByLogin.get());
        page = pageRepository.save(page);
        return qlPageMapper.pageToPageDTO(page);
    }

    public Page<QlPageDTO> findAll(Pageable pageable, Map<String, String> allRequestParams) {
        log.debug("Request to get all QlPageDTO");
        String languageCode = allRequestParams.get("languageCode");
        Page<QlPage> result = pageRepository.findAllByTranslation_languageCode(pageable, languageCode);
        return result.map(qlPageMapper::pageToPageDTO);
    }

    @Transactional(readOnly = true)
    public QlPageDTO findOne(Long id) {
        log.debug("Request to get QlPageDTO: {}", id);
        QlPage result = pageRepository.findOne(id);
        return result != null ? qlPageMapper.pageToPageDTO(result) : null;
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
