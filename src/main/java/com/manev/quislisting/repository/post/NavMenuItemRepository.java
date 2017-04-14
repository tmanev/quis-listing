package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.post.discriminator.NavMenuItem;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NavMenuItemRepository extends PostRepository<NavMenuItem> {
}
