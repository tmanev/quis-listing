package com.manev.quislisting.repository.qlml;

import com.manev.quislisting.domain.qlml.QlString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface QlStringRepository extends JpaRepository<QlString, Long>, QlStringRepositoryCustom {


}
