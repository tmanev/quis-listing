package com.manev.quislisting.domain;

import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ql_nav_menu_item")
public class NavMenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column
    private String title;

    @Column
    private String slug;

    @ManyToOne
    @JoinColumn(name = "static_page_id", referencedColumnName = "id")
    private StaticPage staticPage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "term_taxonomy_id", updatable = false, nullable = false)
    private NavMenu navMenu;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "translation_id")
    private Translation translation;

    @NotNull
    @Column
    private Integer menuOrder;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public StaticPage getStaticPage() {
        return staticPage;
    }

    public void setStaticPage(StaticPage staticPage) {
        this.staticPage = staticPage;
    }

    public NavMenu getNavMenu() {
        return navMenu;
    }

    public void setNavMenu(NavMenu navMenu) {
        this.navMenu = navMenu;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }
}
