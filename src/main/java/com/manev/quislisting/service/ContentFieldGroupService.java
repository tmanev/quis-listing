package com.manev.quislisting.service;

import com.manev.quislisting.domain.ContentFieldGroup;
import com.manev.quislisting.repository.ContentFieldGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContentFieldGroupService {
    private final Logger log = LoggerFactory.getLogger(ContentFieldGroupService.class);
    private ContentFieldGroupRepository contentFieldGroupRepository;

    public ContentFieldGroupService(ContentFieldGroupRepository contentFieldGroupRepository) {
        this.contentFieldGroupRepository = contentFieldGroupRepository;
    }

    public ContentFieldGroup save(ContentFieldGroup contentFieldGroup) {
        return contentFieldGroupRepository.save(contentFieldGroup);
    }

    public Page<ContentFieldGroup> findAll(Pageable pageable) {

        return contentFieldGroupRepository.findAll(pageable);
    }
    public ContentFieldGroup findOne(Long id) {
        log.debug("Request to get PostCategory : {}", id);
        return  contentFieldGroupRepository.findOne(id);
    }
    public void delete(Long id) {
        log.debug("Request to delete PostCategory : {}", id);
        contentFieldGroupRepository.delete(id);
    }
}
