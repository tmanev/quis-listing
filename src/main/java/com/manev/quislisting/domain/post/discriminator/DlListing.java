package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.DlContentFieldRelationship;
import com.manev.quislisting.domain.DlLocationRelationship;
import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@DiscriminatorValue(value = DlListing.TYPE)
public class DlListing extends AbstractPost {
    static final String TYPE = "dl-listing";

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ql_term_post_relationship",
            joinColumns =
            @JoinColumn(name = "term_taxonomy_id", nullable = false, updatable = false),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", nullable = false, updatable = false))
    @Where(clause = "taxonomy='" + DlCategory.TAXONOMY + "'")
    private Set<DlCategory> dlCategories = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ql_post_post_relationship",
            joinColumns =
            @JoinColumn(name = "post_id", nullable = false, updatable = false),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", nullable = false, updatable = false))
    private Set<Attachment> attachments;

    @OneToMany(mappedBy = "dlListing", cascade = CascadeType.ALL)
    private Set<DlContentFieldRelationship> dlContentFieldRelationships;

    @OneToMany(mappedBy = "dlListing", cascade = CascadeType.ALL)
    private Set<DlLocationRelationship> dlLocationRelationships;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    public Set<DlCategory> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(Set<DlCategory> dlCategories) {
        this.dlCategories = dlCategories;
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

    public Attachment removeAttachment(Long attachmentId) {
        if (attachments != null) {
            Iterator<Attachment> iter = attachments.iterator();
            while (iter.hasNext()) {
                Attachment attachment = iter.next();
                if (attachment.getId().equals(attachmentId)) {
                    iter.remove();
                    return attachment;
                }
            }
        }
        return null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<DlContentFieldRelationship> getDlContentFieldRelationships() {
        return dlContentFieldRelationships;
    }

    public void setDlContentFieldRelationships(Set<DlContentFieldRelationship> dlContentFieldRelationships) {
        this.dlContentFieldRelationships = dlContentFieldRelationships;
    }

    public void addDlContentFieldRelationships(DlContentFieldRelationship dlContentFieldRelationshipForSave) {
        if (this.dlContentFieldRelationships == null) {
            this.dlContentFieldRelationships = new HashSet<>();
        }
        this.dlContentFieldRelationships.add(dlContentFieldRelationshipForSave);
    }

    public Set<DlLocationRelationship> getDlLocationRelationships() {
        return dlLocationRelationships;
    }

    public void setDlLocationRelationships(Set<DlLocationRelationship> dlLocationRelationships) {
        this.dlLocationRelationships = dlLocationRelationships;
    }

    public void addDlLocationRelationship(DlLocationRelationship dlLocationRelationship) {
        if (this.dlLocationRelationships == null) {
            this.dlLocationRelationships = new HashSet<>();
        }
        this.dlLocationRelationships.add(dlLocationRelationship);
    }

    public enum Status {
        DRAFT,
        PUBLISH
    }
}
