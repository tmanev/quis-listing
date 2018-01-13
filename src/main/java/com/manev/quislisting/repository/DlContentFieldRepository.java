package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DlContentFieldRepository extends JpaRepository<DlContentField, Long> {

    @Query("SELECT cf from DlContentField cf left join cf.dlCategories dlCategory where (dlCategory is null or dlCategory = :dlCategory) and cf.enabled = :enabled order by cf.orderNum")
    List<DlContentField> findAllByDlCategoriesOrDlCategoriesIsNullAndEnabledOrderByOrderNum(@Param("dlCategory") DlCategory dlCategory, @Param("enabled") Boolean enabled);
}
