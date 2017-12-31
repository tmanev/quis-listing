package com.manev.quislisting.service;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.post.discriminator.builder.DlListingBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.repository.taxonomy.DlLocationRepository;
import com.manev.quislisting.service.dto.UserDTO;
import com.manev.quislisting.service.model.DlContentFieldInput;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.DlListingDTOBuilder;
import com.manev.quislisting.service.post.dto.DlListingFieldDTO;
import com.manev.quislisting.service.post.dto.TranslationDTOBuilder;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import com.manev.quislisting.service.taxonomy.mapper.DlLocationMapper;
import com.manev.quislisting.service.util.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
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
    private static final String SHOULD_NOT_BE_THIS_NAME = "SHOULD_NOT_BE_THIS_NAME";
    private static final String LANGUAGE_CODE_SHOULD_NOT_BE_THIS = "LANGUAGE_CODE_SHOULD_NOT_BE_THIS";
    private static final String SOURCE_LANGUAGE_CODE_SHOULD_NOT_BE_THIS = "SOURCE_LANGUAGE_CODE_SHOULD_NOT_BE_THIS";
    private static Boolean DEFAULT_APPROVED = Boolean.FALSE;
    private static Clock clock = Clock.systemUTC();
    public static final Timestamp ZONED_DATE_TIME_SHOULD_NOT_BE_THIS = new Timestamp(clock.millis());

    @Autowired
    private DlCategoryRepository dlCategoryRepository;

    @Autowired
    private DlLocationRepository dlLocationRepository;

    @Autowired
    private DlContentFieldRepository dlContentFieldRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DlListingRepository dlListingRepository;

    @Autowired
    private DlCategoryTestComponent dlCategoryTestComponent;

    @Autowired
    private DlLocationTestComponent dlLocationTestComponent;

    @Autowired
    private DlContentFieldTestComponent dlContentFieldTestComponent;

    @Autowired
    private DlCategoryMapper dlCategoryMapper;

    @Autowired
    private DlLocationMapper dlLocationMapper;

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

    public DlListingDTO createDlListingDTO(String title, String languageCode, DlCategory dlCategory, DlLocation dlLocation, List<DlContentFieldInput> contentFieldInputs) {
        DlListingDTO dlListingDTO = createDlListingDTO(dlCategory, dlLocation, contentFieldInputs);
        dlListingDTO.setTitle(title);
        dlListingDTO.setName(SlugUtil.slugify(title));
        dlListingDTO.setLanguageCode(languageCode);
        return dlListingDTO;
    }

    public DlListingDTO createDlListingDTO(DlCategory dlCategory, DlLocation dlLocation, List<DlContentFieldInput> contentFieldInputs) {
        DlListingDTO dlListingDTO = DlListingDTOBuilder.aDlListingDTO()
                .withTitle(DlListingTestComponent.DEFAULT_TITLE)
                .withContent(DlListingTestComponent.DEFAULT_CONTENT)
                .withName(SHOULD_NOT_BE_THIS_NAME)
                .withCreated(ZONED_DATE_TIME_SHOULD_NOT_BE_THIS)
                .withModified(ZONED_DATE_TIME_SHOULD_NOT_BE_THIS)
                .withAuthor(new UserDTO(10000L, "some_login", "some first name", "some last name"))
                .withLanguageCode(LANGUAGE_CODE_SHOULD_NOT_BE_THIS)
                .withSourceLanguageCode(SOURCE_LANGUAGE_CODE_SHOULD_NOT_BE_THIS)
                .withTranslationGroupId(TRANSLATION_GROUP_ID_SHOULD_NOT_BE_THIS)
                .addTranslation(TranslationDTOBuilder.aTranslationDTO()
                        .withId(1000L)
                        .build())
                .withStatus(DlListing.Status.PUBLISH_REQUEST)
                .withExpirationDate("2020-01-01")
                .build();

        if (dlCategory != null) {
            dlListingDTO.addDlCategoryDto(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory));
        }

        if (dlLocation != null) {
            dlListingDTO.addDlLocationDto(dlLocationMapper.dlLocationToDlLocationDTO(dlLocation));
        }

        if (contentFieldInputs != null) {
            for (DlContentFieldInput contentFieldInput : contentFieldInputs) {
                dlListingDTO.addDlListingField(new DlListingFieldDTO()
                        .id(contentFieldInput.getDlContentField().getId())
                        .value(contentFieldInput.getValue())
                        .selectedValue(contentFieldInput.getSelectionValue()));
            }
        }

        return dlListingDTO;
    }

}
