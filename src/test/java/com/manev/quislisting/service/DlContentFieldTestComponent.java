package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.web.rest.admin.DlContentFieldAdminRestTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DlContentFieldTestComponent {

    @Autowired
    private DlContentFieldRepository dlContentFieldRepository;

    @Autowired
    private DlContentFieldItemService dlContentFieldItemService;

    public DlContentField createNumberField(DlCategory dlCategory, String name) {
        DlContentField numberContentField = DlContentFieldAdminRestTest.createField(DlContentField.Type.NUMBER, name, 1,
                Collections.singleton(dlCategory));
        numberContentField.qlString(createQlString(numberContentField, DlContentField.Type.NUMBER));
        return dlContentFieldRepository.saveAndFlush(numberContentField);
    }

    public DlContentField createStringField(DlCategory dlCategory, String name) {
        DlContentField stringContentField = DlContentFieldAdminRestTest.createField(DlContentField.Type.STRING, name, 1,
                Collections.singleton(dlCategory));
        stringContentField.qlString(createQlString(stringContentField, DlContentField.Type.STRING));
        return dlContentFieldRepository.saveAndFlush(stringContentField);
    }

    public DlContentField createCheckboxField(DlCategory dlCategory, String name) {
        DlContentField checkboxContentField = DlContentFieldAdminRestTest.createField(DlContentField.Type.CHECKBOX, name, 1,
                Collections.singleton(dlCategory));
        checkboxContentField.qlString(createQlString(checkboxContentField, DlContentField.Type.CHECKBOX));
        return dlContentFieldRepository.saveAndFlush(checkboxContentField);
    }

    private QlString createQlString(DlContentField numberContentField, DlContentField.Type type) {
        return new QlString()
                .languageCode("en")
                .context("dl-content-field")
                .name("dl-content-field-" + type + "-" + numberContentField.getName())
                .value(numberContentField.getName())
                .status(0);
    }

    public DlContentField createSelectField(DlCategory dlCategory, String name, List<String> items) {
        DlContentField selectContentField = DlContentFieldAdminRestTest.createField(DlContentField.Type.SELECT, name, 1,
                Collections.singleton(dlCategory));
        selectContentField.qlString(createQlString(selectContentField, DlContentField.Type.SELECT));

        dlContentFieldRepository.saveAndFlush(selectContentField);

        // add items
        Set<DlContentFieldItem> contentFieldItemSet = new HashSet<>();
        for (String item : items) {
            dlContentFieldItemService.save(new DlContentFieldItemDTO()
                    .value(item)
                    .orderNum(0)
                    .translatedValue(item), selectContentField.getId());
        }

        return selectContentField;
    }
}
