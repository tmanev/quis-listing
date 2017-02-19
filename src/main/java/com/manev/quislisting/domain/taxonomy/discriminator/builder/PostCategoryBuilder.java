package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;

import java.util.Set;

public final class PostCategoryBuilder {
    private Long id;
    private Term term;
    private String description;
    private PostCategory parent;
    private Long count = 0L;

    private PostCategoryBuilder() {
    }

    public static PostCategoryBuilder aPostCategory() {
        return new PostCategoryBuilder();
    }

    public PostCategoryBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PostCategoryBuilder withTerm(Term term) {
        this.term = term;
        return this;
    }

    public PostCategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PostCategoryBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public PostCategoryBuilder withParent(PostCategory parent) {
        this.parent = parent;
        return this;
    }

    public PostCategory build() {
        PostCategory postCategory = new PostCategory();
        postCategory.setId(id);
        postCategory.setTerm(term);
        postCategory.setDescription(description);
        postCategory.setCount(count);
        postCategory.setParent(parent);
        return postCategory;
    }
}
