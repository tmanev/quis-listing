package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Transactional
public interface DlContentFieldItemRepository extends JpaRepository<DlContentFieldItem, Long> {

    Set<DlContentFieldItem> findByIdInOrderByOrderNum(Collection<Long> ids);

    Page<DlContentFieldItem> findAllByDlContentFieldAndParentOrderByOrderNum(Pageable pageable, DlContentField dlContentField, DlContentFieldItem parent);
}
