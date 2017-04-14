package com.manev.quislisting.domain.post.discriminator;

import com.manev.quislisting.domain.post.AbstractPost;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;

import javax.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue(value = NavMenuItem.TYPE)
public class NavMenuItem extends AbstractPost {
    public static final String TYPE = "nav-menu-item";

    @Column
    private Integer postOrder;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "navMenuItems")
    private Set<NavMenu> navMenus;

    @OneToOne
    @JoinColumn(name = "ref_post_id", updatable = false)
    private AbstractPost refPost;

    public Integer getPostOrder() {
        return postOrder;
    }

    public void setPostOrder(Integer postOrder) {
        this.postOrder = postOrder;
    }

    public Set<NavMenu> getNavMenus() {
        return navMenus;
    }

    public void setNavMenus(Set<NavMenu> navMenus) {
        this.navMenus = navMenus;
    }

    public AbstractPost getRefPost() {
        return refPost;
    }

    public void setRefPost(AbstractPost refPost) {
        this.refPost = refPost;
    }
}
