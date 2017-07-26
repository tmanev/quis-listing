package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlAttachmentRepository extends JpaRepository<DlAttachment, Long> {
}
