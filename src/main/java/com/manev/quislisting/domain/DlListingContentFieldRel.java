package com.manev.quislisting.domain;

import com.manev.quislisting.domain.post.discriminator.DlListing;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "ql_dl_listing_dl_content_field_relationship")
public class DlListingContentFieldRel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dl_listing_id", nullable = false, updatable = false)
    private DlListing dlListing;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dl_content_field_id", nullable = false, updatable = false)
    private DlContentField dlContentField;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ql_content_field_selection_relationship",
            joinColumns = @JoinColumn(name = "dl_listing_dl_content_field_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dl_content_field_item_id", referencedColumnName = "id"))
    private Set<DlContentFieldItem> dlContentFieldItems;

    @Column
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlListing getDlListing() {
        return dlListing;
    }

    public void setDlListing(DlListing dlListing) {
        this.dlListing = dlListing;
    }

    public DlContentField getDlContentField() {
        return dlContentField;
    }

    public void setDlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<DlContentFieldItem> getDlContentFieldItems() {
        return dlContentFieldItems;
    }

    public void setDlContentFieldItems(Set<DlContentFieldItem> dlContentFieldItems) {
        this.dlContentFieldItems = dlContentFieldItems;
    }
}
