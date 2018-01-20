package com.manev.quislisting.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "ql_dl_listing_dl_location_relationship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DlListingLocationRel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference(value = "dl_listing_location_listing_reference")
    @ManyToOne(optional = false)
    @JoinColumn(name = "dl_listing_id", nullable = false, updatable = false)
    private DlListing dlListing;

    @ManyToOne(optional = false)
    @JoinColumn(name = "term_taxonomy_id", nullable = false)
    private DlLocation dlLocation;

    @Column
    private String addressLineOne;

    @Column
    private String addressLineTwo;

    @Column
    private String postalCode;

    @Column
    private String additionalInfo;

    @Column
    private Float latitude;

    @Column
    private Float longitude;

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

    public DlLocation getDlLocation() {
        return dlLocation;
    }

    public void setDlLocation(DlLocation dlLocation) {
        this.dlLocation = dlLocation;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }
}
