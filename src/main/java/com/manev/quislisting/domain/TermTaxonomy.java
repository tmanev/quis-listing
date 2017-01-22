package com.manev.quislisting.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ql_term_taxonomy")
public class TermTaxonomy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "term_id")
    private Term term;

    @Column
    private String taxonomy;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TermTaxonomy parent;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(joinColumns = {@JoinColumn(name = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "parent_id")})
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TermTaxonomy> children;

    @Column
    private Integer count = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TermTaxonomy getParent() {
        return parent;
    }

    public void setParent(TermTaxonomy parent) {
        this.parent = parent;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Set<TermTaxonomy> getChildren() {
        return children;
    }

    public void setChildren(Set<TermTaxonomy> children) {
        this.children = children;
    }
}
