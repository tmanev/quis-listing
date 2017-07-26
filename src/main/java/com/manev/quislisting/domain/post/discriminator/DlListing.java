package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.*;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "ql_dl_listing")
public class DlListing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String title;

    @NotNull
    @Column
    private String name;

    @Column
    private String content;

    @Column
    private ZonedDateTime created;

    @Column
    private ZonedDateTime modified;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "translation_id")
    private Translation translation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "ql_dl_listing_dl_category_relationship",
            joinColumns =
            @JoinColumn(name = "dl_listing_id", nullable = false, updatable = false),
            inverseJoinColumns =
            @JoinColumn(name = "term_taxonomy_id", nullable = false, updatable = false))
    @Where(clause = "taxonomy='" + DlCategory.TAXONOMY + "'")
    private Set<DlCategory> dlCategories = new HashSet<>();

    @OneToMany(mappedBy = "dlListing", cascade = CascadeType.ALL)
    private Set<DlAttachment> dlAttachments;

    @OneToMany(mappedBy = "dlListing", cascade = CascadeType.ALL)
    private Set<DlListingContentFieldRel> dlListingContentFieldRels;

    @OneToMany(mappedBy = "dlListing", cascade = CascadeType.ALL)
    private Set<DlListingLocationRel> dlListingLocationRels;

    public Set<DlAttachment> getDlAttachments() {
        return dlAttachments;
    }

    public void setDlAttachments(Set<DlAttachment> dlAttachments) {
        this.dlAttachments = dlAttachments;
    }

    public Set<DlCategory> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(Set<DlCategory> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public void addDlAttachment(DlAttachment dlAttachment) {
        if (this.dlAttachments == null) {
            this.dlAttachments = new HashSet<>();
        }
        this.dlAttachments.add(dlAttachment);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<DlListingContentFieldRel> getDlListingContentFieldRels() {
        return dlListingContentFieldRels;
    }

    public void setDlListingContentFieldRels(Set<DlListingContentFieldRel> dlListingContentFieldRels) {
        this.dlListingContentFieldRels = dlListingContentFieldRels;
    }

    public void addDlContentFieldRelationships(DlListingContentFieldRel dlListingContentFieldRelForSave) {
        if (this.dlListingContentFieldRels == null) {
            this.dlListingContentFieldRels = new HashSet<>();
        }
        this.dlListingContentFieldRels.add(dlListingContentFieldRelForSave);
    }

    public Set<DlListingLocationRel> getDlListingLocationRels() {
        return dlListingLocationRels;
    }

    public void setDlListingLocationRels(Set<DlListingLocationRel> dlListingLocationRels) {
        this.dlListingLocationRels = dlListingLocationRels;
    }

    public void addDlLocationRelationship(DlListingLocationRel dlListingLocationRel) {
        if (this.dlListingLocationRels == null) {
            this.dlListingLocationRels = new HashSet<>();
        }
        this.dlListingLocationRels.add(dlListingLocationRel);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ZonedDateTime getModified() {
        return modified;
    }

    public void setModified(ZonedDateTime modified) {
        this.modified = modified;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DlAttachment removeAttachment(Long attachmentId) {
        if (dlAttachments != null) {
            Iterator<DlAttachment> iter = dlAttachments.iterator();
            while (iter.hasNext()) {
                DlAttachment attachment = iter.next();
                if (attachment.getId().equals(attachmentId)) {
                    iter.remove();
                    return attachment;
                }
            }
        }
        return null;
    }

    public enum Status {
        DRAFT,
        PUBLISH
    }
}
