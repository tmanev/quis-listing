package com.manev.quislisting.service.qlml;

import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.qlml.QlStringRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Set;

@Service
public class QlStringService {

    private final Logger log = LoggerFactory.getLogger(QlStringService.class);

    private final QlStringRepository qlStringRepository;

    public QlStringService(QlStringRepository qlStringRepository) {
        this.qlStringRepository = qlStringRepository;
    }

    public QlString save(QlString qlString) {
        log.debug("Request to save QlString : {}", qlString);
        Set<StringTranslation> stringTranslations = qlString.getStringTranslation();
        for (StringTranslation stringTranslation : stringTranslations) {
            stringTranslation.setQlString(qlString);
            stringTranslation.setTranslationDate(ZonedDateTime.now());
        }
        return qlStringRepository.save(qlString);
    }

    @Transactional(readOnly = true)
    public Page<QlString> findAll(Pageable pageable) {
        log.debug("Request to get all strings");
        return qlStringRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public QlString findOne(Long id) {
        log.debug("Request to get one string : {}", id);
        return qlStringRepository.findOne(id);
    }

}
