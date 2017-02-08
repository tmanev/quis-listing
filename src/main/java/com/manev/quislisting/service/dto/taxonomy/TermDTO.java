package com.manev.quislisting.service.dto.taxonomy;

/**
 * Created by tmanev on 2/7/2017.
 */
public class TermDTO {
    private Long id;
    private String name;
    private String slug;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
