package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adri on 4/4/2017.
 */
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
}
//@Transactional

