package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlListingContentFieldRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlContentFieldRelationshipRepository extends JpaRepository<DlListingContentFieldRel, Long> {

}
