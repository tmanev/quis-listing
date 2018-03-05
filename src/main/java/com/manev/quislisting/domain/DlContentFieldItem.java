package com.manev.quislisting.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.qlml.QlString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "ql_dl_content_field_item")
public class DlContentFieldItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DlContentFieldItem parent;

    @JsonBackReference(value = "dl_content_field_item_children_reference")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<DlContentFieldItem> children;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "dl_content_field_id")
    private DlContentField dlContentField;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "string_id")
    private QlString qlString;

    @NotNull
    @Column
    private String value;

    @NotNull
    @Column
    private Integer orderNum;

    @ManyToOne
    @JoinColumn(name = "dl_content_field_item_group_id")
    private DlContentFieldItemGroup dlContentFieldItemGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlContentFieldItem id(Long id) {
        this.id = id;
        return this;
    }

    public DlContentField getDlContentField() {
        return dlContentField;
    }

    public void setDlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
    }

    public DlContentFieldItem dlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
        return this;
    }

    public QlString getQlString() {
        return qlString;
    }

    public void setQlString(QlString qlString) {
        this.qlString = qlString;
    }

    public DlContentFieldItem qlString(QlString qlString) {
        this.qlString = qlString;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DlContentFieldItem value(String value) {
        this.value = value;
        return this;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public DlContentFieldItem orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public DlContentFieldItem getParent() {
        return parent;
    }

    public void setParent(DlContentFieldItem parent) {
        this.parent = parent;
    }

    public Set<DlContentFieldItem> getChildren() {
        return children;
    }

    public void setChildren(Set<DlContentFieldItem> children) {
        this.children = children;
    }

    public DlContentFieldItemGroup getDlContentFieldItemGroup() {
        return dlContentFieldItemGroup;
    }

    public void setDlContentFieldItemGroup(DlContentFieldItemGroup dlContentFieldItemGroup) {
        this.dlContentFieldItemGroup = dlContentFieldItemGroup;
    }
}
