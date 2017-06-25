package com.manev.quislisting.service.post.exception;

public class PostDifferentLanguageException extends Exception {
    private final String postNameDifferentLanguage;

    public PostDifferentLanguageException(String postNameDifferentLanguage) {
        this.postNameDifferentLanguage = postNameDifferentLanguage;
    }

    public String getPostNameDifferentLanguage() {
        return postNameDifferentLanguage;
    }

}
