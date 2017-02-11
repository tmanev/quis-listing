package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.post.AbstractPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository<T extends AbstractPost> extends JpaRepository<T, Long> {
}
