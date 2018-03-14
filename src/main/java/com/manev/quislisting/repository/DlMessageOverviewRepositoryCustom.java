package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlMessageOverview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DlMessageOverviewRepositoryCustom {
    Page<DlMessageOverview> findAllOverviewMessagesForUser(Pageable pageable, Long userId);
}
