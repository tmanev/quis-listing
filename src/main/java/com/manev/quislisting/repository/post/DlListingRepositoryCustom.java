package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DlListingRepositoryCustom {

    Page<DlListing> findAllForFrontPage(String languageCode, Pageable pageable);

}
