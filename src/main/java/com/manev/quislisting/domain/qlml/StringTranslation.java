package com.manev.quislisting.domain.qlml;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ql_string_translation")
public class StringTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "string_id", nullable = false)
//    private QlString qlString;
//    @Column
//    private Long stringId;

    @Column
    private String languageCode;

    @Column
    private Boolean status;

    @Column
    private String value;

    @Column
    private ZonedDateTime translationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public QlString getQlString() {
//        return qlString;
//    }
//
//    public void setQlString(QlString qlString) {
//        this.qlString = qlString;
//    }


//    public Long getStringId() {
//        return stringId;
//    }
//
//    public void setStringId(Long stringId) {
//        this.stringId = stringId;
//    }

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
