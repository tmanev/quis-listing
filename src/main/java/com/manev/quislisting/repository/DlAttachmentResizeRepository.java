package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlAttachmentResize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlAttachmentResizeRepository extends JpaRepository<DlAttachmentResize, Long> {
}
