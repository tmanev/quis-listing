package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlListingLocationRel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LocationRelationshipRepository extends JpaRepository<DlListingLocationRel, Long> {

}
