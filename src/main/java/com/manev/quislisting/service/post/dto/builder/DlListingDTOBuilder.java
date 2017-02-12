package com.manev.quislisting.service.post.dto.builder;

import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.service.post.dto.Author;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.joda.time.DateTime;

/**
 * Created by tmanev on 2/12/2017.
 */
public final class DlListingDTOBuilder {
    private Long id;
    private String title;
    private String content;
    private String name;
    private DateTime expirationDate;
    private String status;
    private Integer counts;
    private Author author;

    private DlListingDTOBuilder() {
    }

    public static DlListingDTOBuilder aDlListingDTO() {
        return new DlListingDTOBuilder();
    }

    public DlListingDTOBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public DlListingDTOBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public DlListingDTOBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public DlListingDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DlListingDTOBuilder withExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public DlListingDTOBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public DlListingDTOBuilder withCounts(Integer counts) {
        this.counts = counts;
        return this;
    }

    public DlListingDTOBuilder withAuthor(Author author) {
        this.author = author;
        return this;
    }

    public DlListingDTO build() {
        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setId(id);
        dlListingDTO.setTitle(title);
        dlListingDTO.setContent(content);
        dlListingDTO.setName(name);
        dlListingDTO.setExpirationDate(expirationDate);
        dlListingDTO.setStatus(status);
        dlListingDTO.setCounts(counts);
        dlListingDTO.setAuthor(author);
        return dlListingDTO;
    }

}
