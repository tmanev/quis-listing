package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.QDlMessageOverview;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DlMessageOverviewRepositoryImpl implements DlMessageOverviewRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<DlMessageOverview> findAllOverviewMessagesForUser(Pageable pageable, Long userId) {
        JPAQuery<DlMessageOverview> query = new JPAQuery<>(em);

        QDlMessageOverview qDlMessageOverview = QDlMessageOverview.dlMessageOverview;

        query.from(qDlMessageOverview).where(qDlMessageOverview.receiver.id.eq(userId).and(qDlMessageOverview.deleted.isNull()));

        query.limit(pageable.getPageSize());
        query.offset(pageable.getOffset());

        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
