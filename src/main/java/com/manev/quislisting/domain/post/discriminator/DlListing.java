package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue(value = DlListing.TYPE)
public class DlListing extends AbstractPost {
    public static final String TYPE = "dl-listing";
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ql_term_relationship",
            joinColumns =
            @JoinColumn(name = "term_taxonomy_id", nullable = false, updatable = false),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", nullable = false, updatable = false))
    private Set<DlCategory> dlCategories = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ql_term_relationship",
            joinColumns =
            @JoinColumn(name = "term_taxonomy_id", nullable = false, updatable = false),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", nullable = false, updatable = false))
    private Set<DlLocation> dlLocations;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ql_post_relationship",
            joinColumns =
            @JoinColumn(name = "post_id", nullable = false, updatable = false),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", nullable = false, updatable = false))
    private Set<Attachment> attachments;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    public Set<DlCategory> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(Set<DlCategory> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public Set<DlLocation> getDlLocations() {
        return dlLocations;
    }

    public void setDlLocations(Set<DlLocation> dlLocations) {
        this.dlLocations = dlLocations;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        if (attachments == null) {
            attachments = new HashSet<>();
        }
        attachments.add(attachment);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        UNFINISHED,
        PUBLISH
    }
}
