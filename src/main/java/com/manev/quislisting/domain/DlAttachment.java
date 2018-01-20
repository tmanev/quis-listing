package com.manev.quislisting.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ql_dl_attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DlAttachment {

    @OneToMany(mappedBy = "dlAttachment", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DlAttachmentResize> dlAttachmentResizes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String fileName;

    @Column
    private String fileNameSlug;

    @Column
    private String path;

    @Column
    private Integer width;

    @Column
    private Integer height;

    @Column
    private Long size;

    @Column
    private String mimeType;

    @JsonBackReference(value = "dl_attachment_listing_reference")
    @ManyToOne(optional = false)
    @JoinColumn(name = "dl_listing_id", nullable = false, updatable = false)
    private DlListing dlListing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameSlug() {
        return fileNameSlug;
    }

    public void setFileNameSlug(String fileNameSlug) {
        this.fileNameSlug = fileNameSlug;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public DlListing getDlListing() {
        return dlListing;
    }

    public void setDlListing(DlListing dlListing) {
        this.dlListing = dlListing;
    }

    public Set<DlAttachmentResize> getDlAttachmentResizes() {
        return dlAttachmentResizes;
    }

    public void setDlAttachmentResizes(Set<DlAttachmentResize> dlAttachmentResizes) {
        this.dlAttachmentResizes = dlAttachmentResizes;
    }

    public void addDlAttachmentResize(DlAttachmentResize dlAttachmentResize) {
        if (this.dlAttachmentResizes == null) {
            this.dlAttachmentResizes = new HashSet<>();
        }
        this.dlAttachmentResizes.add(dlAttachmentResize);
    }

}
