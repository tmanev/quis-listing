package com.manev.quislisting.repository.qlml;

import com.manev.quislisting.domain.qlml.QQlString;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.service.filter.QlStringFilter;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class QlStringRepositoryImpl implements QlStringRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<QlString> findAllByFilter(QlStringFilter filter, Pageable pageable) {
        JPAQuery<QlString> query = new JPAQuery<>(em);

        QQlString qlString = QQlString.qlString;

        query.from(qlString);

        Long id = filter.getId();
        if (id != null) {
            query.where(qlString.id.eq(id));
        }

        String value = filter.getValue();
        if (!StringUtils.isEmpty(value)) {
            query.where(qlString.value.containsIgnoreCase(value));
        }

        String context = filter.getContext();
        if (!StringUtils.isEmpty(context)) {
            query.where(qlString.context.eq(context));
        }

        query.limit(pageable.getPageSize());
        query.offset(pageable.getOffset());

        return new PageImpl<>(query.fetch(), pageable, query.fetchCount());
    }
}
