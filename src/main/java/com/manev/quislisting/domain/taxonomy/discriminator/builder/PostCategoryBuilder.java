package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.taxonomy.Term;
import com.manev.quislisting.domain.taxonomy.TermTaxonomy;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;

import java.util.Set;

public final class PostCategoryBuilder {
    private Long id;
    private Term term;
    private String taxonomy;
    private String description;
    private Long parentId;
    private Set<TermTaxonomy> children;
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

    public PostCategoryBuilder withTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
        return this;
    }

    public PostCategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PostCategoryBuilder withParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public PostCategoryBuilder withChildren(Set<TermTaxonomy> children) {
        this.children = children;
        return this;
    }

    public PostCategoryBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public PostCategory build() {
        PostCategory postCategory = new PostCategory();
        postCategory.setId(id);
        postCategory.setTerm(term);
        postCategory.setTaxonomy(taxonomy);
        postCategory.setDescription(description);
        postCategory.setParentId(parentId);
        postCategory.setChildren(children);
        postCategory.setCount(count);
        return postCategory;
    }
}
