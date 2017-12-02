package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.QTranslation;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.QDlListing;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DlListingRepositoryImpl implements DlListingRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<DlListing> findAllForFrontPage(String languageCode, Pageable pageable) {
        JPAQuery<DlListing> query = new JPAQuery<>(em);

        QDlListing qDlListing = QDlListing.dlListing;
        QTranslation qTranslation = QTranslation.translation;

        query.from(qDlListing).leftJoin(qDlListing.translation, qTranslation);

        query.where(qDlListing.status.eq(DlListing.Status.PUBLISHED));

        NumberExpression<Integer> confirmExp = new CaseBuilder()
                .when(qTranslation.languageCode.eq(languageCode))
                .then(1)
                .otherwise(2);
        query.orderBy(confirmExp.asc(), qDlListing.modified.desc());

        query.limit(pageable.getPageSize());
        query.offset(pageable.getOffset());

        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
