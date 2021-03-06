package com.manev.quislisting.domain.taxonomy.discriminator.builder;

import com.manev.quislisting.domain.Translation;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;

public final class DlLocationBuilder {
    private Long id;
    private String name;
    private String slug;
    private String taxonomy;
    private String description;
    private Long count = 0L;
    private DlLocation parent;
    private Translation translation;

    private DlLocationBuilder() {
    }

    public static DlLocationBuilder aDlLocation() {
        return new DlLocationBuilder();
    }

    public DlLocationBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlLocationBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlLocationBuilder withSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public DlLocationBuilder withParent(DlLocation parent) {
        this.parent = parent;
        return this;
    }

    public DlLocationBuilder withTranslation(Translation translation) {
        this.translation = translation;
        return this;
    }

    public DlLocationBuilder withTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
        return this;
    }

    public DlLocationBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DlLocationBuilder withCount(Long count) {
        this.count = count;
        return this;
    }

    public DlLocation build() {
        DlLocation dlLocation = new DlLocation();
        dlLocation.setId(id);
        dlLocation.setName(name);
        dlLocation.setSlug(slug);
        dlLocation.setTaxonomy(taxonomy);
        dlLocation.setDescription(description);
        dlLocation.setCount(count);
        dlLocation.setParent(parent);
        dlLocation.setTranslation(translation);
        return dlLocation;
    }
}
