package com.manev.quislisting.domain;

import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ql_dl_content_field")
public class DlContentField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Boolean coreField;

    @Column
    private Integer orderNum;

    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String description;

    @Column
    private String type;

    @Column
    private String iconImage;

    @Column
    private Boolean required;

    @Column
    private Boolean hasConfiguration;

    @Column
    private Boolean hasSearchConfiguration;

    @Column
    private Boolean canBeOrdered;

    @Column
    private Boolean hideName;

    @Column
    private Boolean onExcerptPage;

    @Column
    private Boolean onListingPage;

    @Column
    private Boolean onSearchForm;

    @Column
    private Boolean onMap;

    @Column
    private Boolean onAdvancedSearchForm;

    @ManyToMany
    @JoinTable(name = "ql_term_relationship",
            joinColumns =
            @JoinColumn(name = "term_taxonomy_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "object_id", referencedColumnName = "id"))
    private Set<DlCategory> dlCategories;

    @Column
    private String options;

    @Column
    private String searchOptions;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "string_id")
    private QlString qlString;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="dlContentField")
    public Set<DlContentFieldItem> dlContentFieldItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DlContentField id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getCoreField() {
        return coreField;
    }

    public void setCoreField(Boolean coreField) {
        this.coreField = coreField;
    }

    public DlContentField coreField(Boolean coreField) {
        this.coreField = coreField;
        return this;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public DlContentField orderNum(Integer orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DlContentField name(String name) {
        this.name = name;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public DlContentField slug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DlContentField description(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DlContentField type(String type) {
        this.type = type;
        return this;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public DlContentField iconImage(String iconImage) {
        this.iconImage = iconImage;
        return this;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public DlContentField required(Boolean required) {
        this.required = required;
        return this;
    }

    public Boolean getHasConfiguration() {
        return hasConfiguration;
    }

    public void setHasConfiguration(Boolean hasConfiguration) {
        this.hasConfiguration = hasConfiguration;
    }

    public DlContentField hasConfiguration(Boolean hasConfiguration) {
        this.hasConfiguration = hasConfiguration;
        return this;
    }

    public Boolean getHasSearchConfiguration() {
        return hasSearchConfiguration;
    }

    public void setHasSearchConfiguration(Boolean hasSearchConfiguration) {
        this.hasSearchConfiguration = hasSearchConfiguration;
    }

    public DlContentField hasSearchConfiguration(Boolean hasSearchConfiguration) {
        this.hasSearchConfiguration = hasSearchConfiguration;
        return this;
    }

    public Boolean getCanBeOrdered() {
        return canBeOrdered;
    }

    public void setCanBeOrdered(Boolean canBeOrdered) {
        this.canBeOrdered = canBeOrdered;
    }

    public DlContentField canBeOrdered(Boolean canBeOrdered) {
        this.canBeOrdered = canBeOrdered;
        return this;
    }

    public Boolean getHideName() {
        return hideName;
    }

    public void setHideName(Boolean hideName) {
        this.hideName = hideName;
    }

    public DlContentField hideName(Boolean hideName) {
        this.hideName = hideName;
        return this;
    }

    public Boolean getOnExcerptPage() {
        return onExcerptPage;
    }

    public void setOnExcerptPage(Boolean onExcerptPage) {
        this.onExcerptPage = onExcerptPage;
    }

    public DlContentField onExcerptPage(Boolean onExcerptPage) {
        this.onExcerptPage = onExcerptPage;
        return this;
    }

    public Boolean getOnListingPage() {
        return onListingPage;
    }

    public void setOnListingPage(Boolean onListingPage) {
        this.onListingPage = onListingPage;
    }

    public DlContentField onListingPage(Boolean onListingPage) {
        this.onListingPage = onListingPage;
        return this;
    }

    public Boolean getOnSearchForm() {
        return onSearchForm;
    }

    public void setOnSearchForm(Boolean onSearchForm) {
        this.onSearchForm = onSearchForm;
    }

    public DlContentField onSearchForm(Boolean onSearchForm) {
        this.onSearchForm = onSearchForm;
        return this;
    }

    public Boolean getOnMap() {
        return onMap;
    }

    public void setOnMap(Boolean onMap) {
        this.onMap = onMap;
    }

    public DlContentField onMap(Boolean onMap) {
        this.onMap = onMap;
        return this;
    }

    public Boolean getOnAdvancedSearchForm() {
        return onAdvancedSearchForm;
    }

    public void setOnAdvancedSearchForm(Boolean onAdvancedSearchForm) {
        this.onAdvancedSearchForm = onAdvancedSearchForm;
    }

    public DlContentField onAdvancedSearchForm(Boolean onAdvancedSearchForm) {
        this.onAdvancedSearchForm = onAdvancedSearchForm;
        return this;
    }

    public Set<DlCategory> getDlCategories() {
        return dlCategories;
    }

    public void setDlCategories(Set<DlCategory> dlCategories) {
        this.dlCategories = dlCategories;
    }

    public DlContentField dlCategories(Set<DlCategory> dlCategories) {
        this.dlCategories = dlCategories;
        return this;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public DlContentField options(String options) {
        this.options = options;
        return this;
    }

    public String getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(String searchOptions) {
        this.searchOptions = searchOptions;
    }

    public DlContentField searchOptions(String searchOptions) {
        this.searchOptions = searchOptions;
        return this;
    }

    public QlString getQlString() {
        return qlString;
    }

    public void setQlString(QlString qlString) {
        this.qlString = qlString;
    }

    public DlContentField qlString(QlString qlString) {
        this.qlString = qlString;
        return this;
    }

    public Set<DlContentFieldItem> getDlContentFieldItems() {
        return dlContentFieldItems;
    }

    public void setDlContentFieldItems(Set<DlContentFieldItem> dlContentFieldItems) {
        this.dlContentFieldItems = dlContentFieldItems;
    }

    public DlContentField dlContentFieldItems(Set<DlContentFieldItem> dlContentFieldItems) {
        this.dlContentFieldItems = dlContentFieldItems;
        return this;
    }
}
