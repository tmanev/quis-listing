package com.manev.quislisting.repository.post;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.AbstractPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository<T extends AbstractPost> extends JpaRepository<T, Long> {

    Page<T> findAllByTranslation_languageCodeAndUser(Pageable pageable, String languageCode, User user);

    Page<T> findAllByTranslation_languageCode(Pageable pageable, String languageCode);

    Long countByTranslation_languageCode(String languageCode);

    T findOneByName(String name);

    T findOneByTranslation(Translation translationForLanguage);
}
