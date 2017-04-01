package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DlListingRepository extends PostRepository<DlListing> {
}
