package com.manev.quislisting.repository;

import com.manev.quislisting.domain.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adri on 4/4/2017.
 */
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
}

