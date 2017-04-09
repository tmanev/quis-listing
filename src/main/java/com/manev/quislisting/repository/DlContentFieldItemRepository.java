package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DlContentFieldItemRepository extends JpaRepository<DlContentFieldItem, Long> {
    List<DlContentFieldItem> findAllByDlContentField(DlContentField dlContentField);
}
