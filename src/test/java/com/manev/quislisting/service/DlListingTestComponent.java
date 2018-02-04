package com.manev.quislisting.service;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.service.form.DlCategoryForm;
import com.manev.quislisting.service.form.DlListingFieldForm;
import com.manev.quislisting.service.form.DlListingForm;
import com.manev.quislisting.service.form.DlLocationForm;
import com.manev.quislisting.service.model.DlContentFieldInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Component
public class DlListingTestComponent {

    public static final String DEFAULT_TITLE = "DEFAULT_TITLE";
    public static final String DEFAULT_CONTENT = "DEFAULT_CONTENT";
    public static final String DEFAULT_NAME = "default_title";
    public static final DlListing.Status DEFAULT_STATUS = DlListing.Status.DRAFT;
    public static final String DEFAULT_LANGUAGE_CODE = "en";
    public static final long TRANSLATION_GROUP_ID_SHOULD_NOT_BE_THIS = 10000L;
    private static final Timestamp DEFAULT_CREATED = Timestamp.valueOf(LocalDateTime.parse("2007-12-03T10:15:30"));
    private static final Timestamp DEFAULT_MODIFIED = Timestamp.valueOf(LocalDateTime.parse("2007-12-03T10:15:30"));
    private static Boolean DEFAULT_APPROVED = Boolean.FALSE;
    private static Clock clock = Clock.systemUTC();
    public static final Timestamp ZONED_DATE_TIME_SHOULD_NOT_BE_THIS = new Timestamp(clock.millis());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DlListingRepository dlListingRepository;

    public static DlListing createEntity() {
        return DlListingBuilder.aDlListing()
                .withTitle(DEFAULT_TITLE)
                .withContent(DEFAULT_CONTENT)
                .withName(DEFAULT_NAME)
                .withCreated(DEFAULT_CREATED)
                .withModified(DEFAULT_MODIFIED)
                .withStatus(DEFAULT_STATUS)
                .withApproved(DEFAULT_APPROVED)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode(DEFAULT_LANGUAGE_CODE)
                        .withTranslationGroup(new TranslationGroup())
                        .build())
                .build();
    }

    public DlListing createDlListing(DlCategory dlCategory) {
        // setup dlListing
        DlListing dlListing = createEntity();
        dlListing.setUser(userRepository.findOneByLogin("user").orElse(null));

        dlListing.setDlCategories(new HashSet<DlCategory>() {{
            add(dlCategory);
        }});

        return dlListingRepository.saveAndFlush(dlListing);
    }

    public DlListingForm createDlListingForm(String title, DlCategory dlCategory, DlLocation dlLocation, List<DlContentFieldInput> contentFieldInputs) {
        DlListingForm dlListingForm = createDlListingForm(dlCategory, dlLocation, contentFieldInputs);
        dlListingForm.setTitle(title);
        return dlListingForm;
    }

    public DlListingForm createDlListingForm(DlCategory dlCategory, DlLocation dlLocation,
                                              List<DlContentFieldInput> contentFieldInputs) {
        DlListingForm dlListingForm = new DlListingForm();
        dlListingForm.setTitle(DlListingTestComponent.DEFAULT_TITLE);
        dlListingForm.setContent(DlListingTestComponent.DEFAULT_CONTENT);

        if (dlCategory != null) {
            DlCategoryForm dlCategoryForm = new DlCategoryForm();
            dlCategoryForm.setId(dlCategory.getId());
            dlListingForm.setDlCategories(new ArrayList<>(Collections.singletonList(dlCategoryForm)));
        }

        if (dlLocation != null) {
            DlLocationForm dlLocationForm = new DlLocationForm();
            dlLocationForm.setId(dlLocation.getId());
            dlListingForm.setDlLocations(new ArrayList<>(Collections.singletonList(dlLocationForm)));
        }

        if (!CollectionUtils.isEmpty(contentFieldInputs)) {
            List<DlListingFieldForm> inputs = new ArrayList<>();
            for (DlContentFieldInput contentFieldInput : contentFieldInputs) {
                DlListingFieldForm dlListingFieldForm = new DlListingFieldForm();
                dlListingFieldForm.setId(contentFieldInput.getDlContentField().getId());
                dlListingFieldForm.setValue(contentFieldInput.getValue());
                dlListingFieldForm.setSelectedValue(contentFieldInput.getSelectionValue());

                inputs.add(dlListingFieldForm);
            }
            dlListingForm.setDlListingFields(inputs);
        }

        return dlListingForm;
    }

}
