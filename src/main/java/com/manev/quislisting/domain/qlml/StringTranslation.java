package com.manev.quislisting.domain.qlml;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ql_string_translation")
public class StringTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "string_id", nullable = false, updatable = false)
    private QlString qlString;

    @NotNull
    @Column
    private String languageCode;

    @NotNull
    @Column
    private Boolean status;

    @NotNull
    @Column
    private String value;

    @NotNull
    @Column
    private ZonedDateTime translationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QlString getQlString() {
        return qlString;
    }

    public void setQlString(QlString qlString) {
        this.qlString = qlString;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getTranslationDate() {
        return translationDate;
    }

    public void setTranslationDate(ZonedDateTime translationDate) {
        this.translationDate = translationDate;
    }
}
