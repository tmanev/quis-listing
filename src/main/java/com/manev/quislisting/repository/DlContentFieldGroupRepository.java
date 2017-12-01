package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlContentFieldGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlContentFieldGroupRepository extends JpaRepository<DlContentFieldGroup, Long> {

}
