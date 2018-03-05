package com.manev.quislisting.domain;

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
@Table(name = "ql_dl_content_field_item_group")
public class DlContentFieldItemGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String name;

    @Column
    private String description;

    @NotNull
    @Column
    private Integer orderNum;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "string_id")
    private QlString qlString;

    @OneToMany(mappedBy="dlContentFieldItemGroup")
    private Set<DlContentFieldItem> dlContentFieldItems;

    @ManyToOne
    @JoinColumn(name="dl_content_field_id", nullable=false, updatable=false)
    private DlContentField dlContentField;

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

    public Set<DlContentFieldItem> getDlContentFieldItems() {
        return dlContentFieldItems;
    }

    public void setDlContentFieldItems(Set<DlContentFieldItem> dlContentFieldItems) {
        this.dlContentFieldItems = dlContentFieldItems;
    }

    public DlContentField getDlContentField() {
        return dlContentField;
    }

    public void setDlContentField(DlContentField dlContentField) {
        this.dlContentField = dlContentField;
    }

    public QlString getQlString() {
        return qlString;
    }

    public void setQlString(QlString qlString) {
        this.qlString = qlString;
    }
}
