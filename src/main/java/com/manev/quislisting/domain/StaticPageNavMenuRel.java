package com.manev.quislisting.domain;

import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ql_static_page_nav_menu_relationship")
public class StaticPageNavMenuRel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "static_page_id", updatable = false, nullable = false)
    private StaticPage staticPage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "term_taxonomy_id", updatable = false, nullable = false)
    private NavMenu navMenu;

    @NotNull
    @Column
    private Integer menuOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
