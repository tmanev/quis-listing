package com.manev.quislisting.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ql_dl_content_field")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DlContentField {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private Boolean coreField;

    @NotNull
    @Column
    private Integer orderNum;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String slug;

    @Column
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Type type;

    @Column
    private String iconImage;

    @NotNull
    @Column
    private Boolean required;

    @NotNull
    @Column
    private Boolean hasConfiguration;

    @NotNull
    @Column
    private Boolean hasSearchConfiguration;

    @NotNull
    @Column
    private Boolean canBeOrdered;

    @NotNull
    @Column
    private Boolean hideName;

    @NotNull
    @Column
    private Boolean onExcerptPage;

    @NotNull
    @Column
    private Boolean onListingPage;

    @NotNull
    @Column
    private Boolean onSearchForm;

    @NotNull
    @Column
    private Boolean onMap;

    @NotNull
    @Column
    private Boolean onAdvancedSearchForm;

    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "ql_dl_category_dl_content_field_relationship",
            joinColumns = @JoinColumn(name = "dl_content_field_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "term_taxonomy_id", referencedColumnName = "id"))
    private Set<DlCategory> dlCategories;

    @Column
    private String options;

    @Column
    private String searchOptions;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "string_id")
    private QlString qlString;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "string_description_id")
    private QlString qlStringDescription;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dlContentField")
    @OrderBy("orderNum asc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DlContentFieldItem> dlContentFieldItems;

    @ManyToOne
    @JoinColumn(name = "dl_content_field_group_id")
    private DlContentFieldGroup dlContentFieldGroup;

    @Column
    private Boolean enabled;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public DlContentField type(Type type) {
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

    public QlString getQlStringDescription() {
        return qlStringDescription;
    }

    public void setQlStringDescription(QlString qlStringDescription) {
        this.qlStringDescription = qlStringDescription;
    }

    public DlContentField qlStringDescription(QlString qlStringDescription) {
        this.qlStringDescription = qlStringDescription;
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

    public void addDlContentFieldItem(DlContentFieldItem dlContentFieldItem) {
        if (this.dlContentFieldItems == null) {
            this.dlContentFieldItems = new HashSet<>();
        }
        this.dlContentFieldItems.add(dlContentFieldItem);
    }

    public DlContentFieldGroup getDlContentFieldGroup() {
        return dlContentFieldGroup;
    }

    public void setDlContentFieldGroup(DlContentFieldGroup dlContentFieldGroup) {
        this.dlContentFieldGroup = dlContentFieldGroup;
    }

    public DlContentField dlContentFieldGroup(DlContentFieldGroup dlContentFieldGroup) {
        this.dlContentFieldGroup = dlContentFieldGroup;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public DlContentField enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public enum Type {
        STRING, TEXT_AREA, NUMBER, SELECT, DEPENDENT_SELECT, RADIO, CHECKBOX, WEBSITE, EMAIL, OPEN_HOURS,
        NUMBER_UNIT
    }

}
