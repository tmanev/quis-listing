package com.manev.quislisting.repository.qlml;

import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.service.filter.QlStringFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QlStringRepositoryCustom {

    Page<QlString> findAllByFilter(QlStringFilter filter, Pageable pageable);

}
