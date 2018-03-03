package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItemGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlContentFieldItemGroupRepository extends JpaRepository<DlContentFieldItemGroup, Long> {

    Page<DlContentFieldItemGroup> findAllByDlContentField(Pageable pageable, DlContentField dlContentField);
}
