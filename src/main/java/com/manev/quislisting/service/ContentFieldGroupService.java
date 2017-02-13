package com.manev.quislisting.service;

import com.manev.quislisting.domain.ContentFieldGroup;
import com.manev.quislisting.repository.ContentFieldGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContentFieldGroupService {

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
}
