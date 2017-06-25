package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.service.post.exception.PostDifferentLanguageException;
import com.manev.quislisting.service.post.exception.PostNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AbstractPostService {

    private final PostRepository<AbstractPost> postRepository;

    public AbstractPostService(PostRepository<AbstractPost> postRepository) {
        this.postRepository = postRepository;
    }

    public AbstractPost retrievePost(String languageCode, String postId) {
        AbstractPost post = postRepository.findOne(Long.valueOf(postId));
        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(languageCode)) {
            Translation translationForLanguage = translationExists(languageCode, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                post = postRepository.findOneByTranslation(translationForLanguage);
            }
        }

        return post;
    }

    public AbstractPost findOneByName(String name) {
        return postRepository.findOneByName(name);
    }

    public AbstractPost findOneByName(String name, String language) throws PostNotFoundException, PostDifferentLanguageException {
        AbstractPost post = postRepository.findOneByName(name);

        if (post == null) {
            throw new PostNotFoundException(String.format("Post with name: %s not found", name));
        }

        Translation translation = post.getTranslation();
        if (!translation.getLanguageCode().equals(language)) {
            // find post related to the translation
            // and redirect it to the page url

            // check if there is a translation
            Translation translationForLanguage = translationExists(language, translation.getTranslationGroup().getTranslations());
            if (translationForLanguage != null) {
                AbstractPost translatedPost = postRepository.findOneByTranslation(translationForLanguage);
                throw new PostDifferentLanguageException((translatedPost.getName()));
            }
        }

        return post;
    }

    private Translation translationExists(String languageCode, Set<Translation> translationList) {
        for (Translation translation : translationList) {
            if (translation.getLanguageCode().equals(languageCode)) {
                return translation;
            }
        }
        return null;
    }

    public AbstractPost findOne(Long id) {
        return postRepository.findOne(id);
    }
}
