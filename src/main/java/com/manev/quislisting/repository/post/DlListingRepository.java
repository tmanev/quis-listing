package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlListingRepository extends JpaRepository<DlListing, Long>, DlListingRepositoryCustom {

    Page<DlListing> findAllByTranslation_languageCode(Pageable pageable, String languageCode);

    Long countByTranslation_languageCode(String languageCode);

    Page<DlListing> findAllByTranslation_languageCodeAndUser(Pageable pageable, String languageCode, User user);

}
