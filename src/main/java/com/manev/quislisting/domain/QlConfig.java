package com.manev.quislisting.domain;

import javax.persistence.*;

@Entity
@Table(name = "ql_config")
public class QlConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String qlKey;

    @Column
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQlKey() {
        return qlKey;
    }

    public void setQlKey(String qlKey) {
        this.qlKey = qlKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
