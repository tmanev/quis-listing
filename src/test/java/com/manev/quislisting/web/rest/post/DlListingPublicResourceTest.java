package com.manev.quislisting.web.rest.post;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.DlLocation;
import com.manev.quislisting.service.DlCategoryTestComponent;
import com.manev.quislisting.service.DlContentFieldTestComponent;
import com.manev.quislisting.service.DlListingTestComponent;
import com.manev.quislisting.service.DlLocationTestComponent;
import com.manev.quislisting.service.model.DlContentFieldInput;
import com.manev.quislisting.service.post.DlListingService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.web.rest.GenericResourceTest;
import com.manev.quislisting.web.rest.TestUtil;
import com.manev.quislisting.web.rest.post.filter.DlContentFieldFilter;
import com.manev.quislisting.web.rest.post.filter.DlListingSearchFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static com.manev.quislisting.web.rest.RestRouter.RESOURCE_API_PUBLIC_DL_LISTINGS;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlListingPublicResourceTest extends GenericResourceTest {

    @Autowired
    private DlListingService dlListingService;

    @Autowired
    private DlCategoryTestComponent dlCategoryTestComponent;

    @Autowired
    private DlLocationTestComponent dlLocationTestComponent;

    @Autowired
    private DlListingTestComponent dlListingTestComponent;
    @Autowired
    private DlContentFieldTestComponent dlContentFieldTestComponent;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;
    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private LocaleResolver localeResolver;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlListingPublicResource dlListingPublicResource = new DlListingPublicResource(dlListingService, localeResolver);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dlListingPublicResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByTitleOrDescription() throws Exception {
        DlListingDTO savedDlListingDTO = createDlListingDTO();
        dlListingService.approveListing(savedDlListingDTO.getId());

        DlListingSearchFilter dlListingSearchFilter = new DlListingSearchFilter();
        dlListingSearchFilter.setText(DlListingTestComponent.DEFAULT_TITLE);
        dlListingSearchFilter.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilter);
        mockMvc.perform(get(RESOURCE_API_PUBLIC_DL_LISTINGS + "/_search?query=" + URLEncoder.encode(query, "UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedDlListingDTO.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DlListingTestComponent.DEFAULT_TITLE)));
    }

    private DlListingDTO createDlListingDTO() {
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlLocation dlLocation = dlLocationTestComponent.initDlLocation();
        DlContentField priceCF = dlContentFieldTestComponent.createNumberField(dlCategory, "Number-price");
        DlContentField dlPhoneCF = dlContentFieldTestComponent.createStringField(dlCategory, "String-Phone");

        DlListingDTO dlListingDTO = dlListingTestComponent.createDlListingDTO(dlCategory, dlLocation, new ArrayList<DlContentFieldInput>() {{
            add(new DlContentFieldInput(priceCF, "180"));
            add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
        }});

        return dlListingService.save(dlListingDTO, "en");
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByCategory() throws Exception {
        DlCategory dlCategory1EN = dlCategoryTestComponent.initCategory("Category 1 EN", "en");
        DlCategory dlCategory11EN = dlCategoryTestComponent.initCategory(dlCategory1EN, "Category 11 EN", "en");
        DlCategory dlCategory2EN = dlCategoryTestComponent.initCategory("Category 2 EN", "en");
        DlCategory dlCategory21EN = dlCategoryTestComponent.initCategory(dlCategory2EN, "Category 21 EN", "en");
        DlCategory dlCategory22EN = dlCategoryTestComponent.initCategory(dlCategory2EN, "Category 22 EN", "en");

        DlCategory dlCategory1BG = dlCategoryTestComponent.initCategory(dlCategory1EN.getTranslation().getTranslationGroup().getId(), "Category 1 BG", "bg");
        DlCategory dlCategory11BG = dlCategoryTestComponent.initCategory(dlCategory11EN.getTranslation().getTranslationGroup().getId(), dlCategory1BG, "Category 11 BG", "bg");
        DlCategory dlCategory2BG = dlCategoryTestComponent.initCategory(dlCategory2EN.getTranslation().getTranslationGroup().getId(), "Category 2 BG", "bg");
        DlCategory dlCategory21BG = dlCategoryTestComponent.initCategory(dlCategory21EN.getTranslation().getTranslationGroup().getId(), dlCategory2BG, "Category 21 BG", "bg");
        DlCategory dlCategory22BG = dlCategoryTestComponent.initCategory(dlCategory22EN.getTranslation().getTranslationGroup().getId(), dlCategory2BG, "Category 22 BG", "bg");

        DlListingDTO savedDlListing1 = dlListingService.save(dlListingTestComponent.createDlListingDTO("Listing One", "en",
                dlCategory11EN, null, null), "en");
        dlListingService.approveListing(savedDlListing1.getId());
        DlListingDTO savedDlListing2 = dlListingService.save(dlListingTestComponent.createDlListingDTO("Listing Two", "en",
                dlCategory21EN, null, null), "en");
        dlListingService.approveListing(savedDlListing2.getId());
        DlListingDTO savedDlListing3 = dlListingService.save(dlListingTestComponent.createDlListingDTO("Listing Three", "bg",
                dlCategory21BG, null, null), "bg");
        dlListingService.approveListing(savedDlListing3.getId());
        DlListingDTO savedDlListingDTO4 = dlListingService.save(dlListingTestComponent.createDlListingDTO("Listing Four", "bg",
                dlCategory22BG, null, null), "bg");
        dlListingService.approveListing(savedDlListingDTO4.getId());
        DlListingDTO savedDlListingDTO5 = dlListingService.save(dlListingTestComponent.createDlListingDTO("Listing Five", "bg",
                dlCategory22BG, null, null), "bg");
        dlListingService.approveListing(savedDlListingDTO5.getId());

        // search by category
        DlListingSearchFilter dlListingSearchFilterEN = new DlListingSearchFilter();
        dlListingSearchFilterEN.setCategoryId(String.valueOf(dlCategory21EN.getId()));
        dlListingSearchFilterEN.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilterEN);
        ResultActions perform = mockMvc.perform(get(RESOURCE_API_PUBLIC_DL_LISTINGS + "/_search?query=" + URLEncoder.encode(query, "UTF-8")));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[0].id").value(savedDlListing2.getId().intValue()))
                .andExpect(jsonPath("$.[1].id").value(savedDlListing3.getId().intValue()))
                .andExpect(header().string("X-Total-Count", "2"));

        // search by category on different language
        DlListingSearchFilter dlListingSearchFilterBG = new DlListingSearchFilter();
        dlListingSearchFilterBG.setCategoryId(String.valueOf(dlCategory21BG.getId()));
        dlListingSearchFilterBG.setLanguageCode("bg");

        String queryBG = TestUtil.convertObjectToJsonString(dlListingSearchFilterBG);
        mockMvc.perform(get(RESOURCE_API_PUBLIC_DL_LISTINGS + "/_search?query=" + URLEncoder.encode(queryBG, "UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[0].id").value(savedDlListing3.getId().intValue()))
                .andExpect(jsonPath("$.[1].id").value(savedDlListing2.getId().intValue()))
                .andExpect(header().string("X-Total-Count", "2"));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByLocation() throws Exception {
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlLocation dlLocation = dlLocationTestComponent.initDlLocation();
        DlLocation dlLocation2 = dlLocationTestComponent.initDlLocation("Second location");

        DlListingDTO dlListingDTO = dlListingTestComponent.createDlListingDTO(dlCategory, dlLocation, null);
        DlListingDTO dlListingDTO2 = dlListingTestComponent.createDlListingDTO(dlCategory, dlLocation2, null);

        DlListingDTO savedDlListingDTO1 = dlListingService.save(dlListingDTO, "en");
        dlListingService.approveListing(savedDlListingDTO1.getId());
        DlListingDTO savedDlListingDTO2 = dlListingService.save(dlListingDTO2, "en");
        dlListingService.approveListing(savedDlListingDTO2.getId());

        DlListingSearchFilter dlListingSearchFilter = new DlListingSearchFilter();
        dlListingSearchFilter.setLocationId(String.valueOf(dlLocation2.getId()));
        dlListingSearchFilter.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilter);
        ResultActions perform = mockMvc.perform(get(RESOURCE_API_PUBLIC_DL_LISTINGS + "/_search?query=" + URLEncoder.encode(query, "UTF-8")));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedDlListingDTO2.getId().intValue())))
                .andExpect(header().string("X-Total-Count", "1"));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void shouldSearchByContentFields() throws Exception {
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlContentField priceCF = dlContentFieldTestComponent.createNumberField(dlCategory, "Number-price");
        DlContentField dlPhoneCF = dlContentFieldTestComponent.createStringField(dlCategory, "String-Phone");
        DlContentField dlFuelCF = dlContentFieldTestComponent.createSelectField(dlCategory, "Select-Fuel", Arrays.asList("Diesel", "Gasoline"));

        DlListingDTO dlListingDTO = dlListingTestComponent.createDlListingDTO(dlCategory, null, new ArrayList<DlContentFieldInput>() {{
            add(new DlContentFieldInput(priceCF, "180"));
            add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
            add(new DlContentFieldInput(dlFuelCF, null, String.valueOf(findByName("Diesel", dlFuelCF.getDlContentFieldItems()).getId())));
        }});

        DlListingDTO dlListingDTO2 = dlListingTestComponent.createDlListingDTO(dlCategory, null, new ArrayList<DlContentFieldInput>() {{
            add(new DlContentFieldInput(priceCF, "150"));
            add(new DlContentFieldInput(dlPhoneCF, "+123 456 555"));
            add(new DlContentFieldInput(dlFuelCF, null, String.valueOf(findByName("Gasoline", dlFuelCF.getDlContentFieldItems()).getId())));
        }});

        DlListingDTO savedDlListingDTO1 = dlListingService.save(dlListingDTO, "en");
        dlListingService.approveListing(savedDlListingDTO1.getId());
        DlListingDTO savedDlListingDTO2 = dlListingService.save(dlListingDTO2, "en");
        dlListingService.approveListing(savedDlListingDTO2.getId());

        DlListingSearchFilter dlListingSearchFilter = new DlListingSearchFilter();
        dlListingSearchFilter.setContentFields(Arrays.asList(new DlContentFieldFilter(dlFuelCF.getId(), null, String.valueOf(findByName("Gasoline", dlFuelCF.getDlContentFieldItems()).getId()))));
        dlListingSearchFilter.setLanguageCode("en");

        String query = TestUtil.convertObjectToJsonString(dlListingSearchFilter);
        ResultActions perform = mockMvc.perform(get(RESOURCE_API_PUBLIC_DL_LISTINGS + "/_search?query=" + URLEncoder.encode(query, "UTF-8")));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(savedDlListingDTO2.getId().intValue())))
                .andExpect(header().string("X-Total-Count", "1"));
    }

    private DlContentFieldItem findByName(String item, Set<DlContentFieldItem> items) {
        for (DlContentFieldItem dlContentFieldItem : items) {
            if (item.equals(dlContentFieldItem.getValue())) {
                return dlContentFieldItem;
            }
        }
        return null;
    }

}