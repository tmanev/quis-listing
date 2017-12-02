package com.manev.quislisting.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "ql_dl_content_field_group")
public class DlContentFieldGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String slug;

    @Column
    private String description;

    @NotNull
    @Column
    private Integer orderNum;

    @JsonBackReference(value = "dl_content_fields_reference")
    @OneToMany(mappedBy="dlContentFieldGroup")
    private Set<DlContentField> dlContentFields;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Set<DlContentField> getDlContentFields() {
        return dlContentFields;
    }

    public void setDlContentFields(Set<DlContentField> dlContentFields) {
        this.dlContentFields = dlContentFields;
    }
}
