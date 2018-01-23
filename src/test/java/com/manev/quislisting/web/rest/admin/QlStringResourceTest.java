package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.qlml.QlStringRepository;
import com.manev.quislisting.service.qlml.QlStringService;
import com.manev.quislisting.web.rest.AdminRestRouter;
import com.manev.quislisting.web.rest.TestUtil;
import com.manev.quislisting.web.rest.admin.QlStringResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class QlStringResourceTest {


    private static final String DEFAULT_LCODE = "en";
    private static final String DEFAULT_CONTEXT = "default context";
    private static final String DEFAULT_NAME = "default name";
    private static final String DEFAULT_VALUE = "default value";
    private static final String DEFAULT_TYPE = "default type";
    private static final Integer DEFAULT_STATUS = 0;

    @Autowired
    private QlStringRepository qlStringRepository;

    @Autowired
    private QlStringService qlStringService;

    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    private MockMvc mockMvc;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;

    private QlString qlString;

    public static QlString createEntity() {
        QlString qlString = new QlString();
        qlString.setName(DEFAULT_NAME);
        qlString.setValue(DEFAULT_VALUE);
        qlString.setContext(DEFAULT_CONTEXT);
        qlString.setLanguageCode(DEFAULT_LCODE);
        qlString.setStatus(DEFAULT_STATUS);

        return qlString;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QlStringResource qlStringResource = new QlStringResource(qlStringService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(qlStringResource)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        qlString = createEntity();
    }

    @Test
    @Transactional
    public void getQlStrings() throws Exception {
        qlStringRepository.saveAndFlush(qlString);

        mockMvc.perform(get(AdminRestRouter.QlString.LIST))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(qlString.getId().intValue())))
                .andExpect(jsonPath("$.[*].languageCode").value(hasItem(DEFAULT_LCODE)))
                .andExpect(jsonPath("$.[*].name").value(hasItem((DEFAULT_NAME))))
                .andExpect(jsonPath("$.[*].context").value(hasItem((DEFAULT_CONTEXT))))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getQlStringsShouldFilterByValue() throws Exception {
        qlStringRepository.saveAndFlush(qlString);

        mockMvc.perform(get(AdminRestRouter.QlString.LIST)
                .param("value", DEFAULT_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[0].id").value((qlString.getId().intValue())))
                .andExpect(jsonPath("$.[0].languageCode").value((DEFAULT_LCODE)))
                .andExpect(jsonPath("$.[0].name").value(((DEFAULT_NAME))))
                .andExpect(jsonPath("$.[0].context").value(((DEFAULT_CONTEXT))))
                .andExpect(jsonPath("$.[0].value").value((DEFAULT_VALUE)))
                .andExpect(jsonPath("$.[0].status").value((DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getQlString() throws Exception {
        qlStringRepository.saveAndFlush(qlString);

        mockMvc.perform(get(AdminRestRouter.QlString.DETAIL, qlString.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(qlString.getId().intValue()))
                .andExpect(jsonPath("$.languageCode").value(DEFAULT_LCODE))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.context").value(DEFAULT_CONTEXT))
                .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
                .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingQlString() throws Exception {
        mockMvc.perform(get(AdminRestRouter.QlString.DETAIL, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQlString() throws Exception {
        qlStringRepository.saveAndFlush(qlString);
        int databaseSizeBeforeUpdate = qlStringRepository.findAll().size();

        QlString updateQlString = qlStringRepository.findOne(this.qlString.getId());

        Set<StringTranslation> stringTranslationSet = new HashSet<>();
        stringTranslationSet.add(createStringTranslation("telefon", "mk"));
        stringTranslationSet.add(createStringTranslation("Telefon", "de"));
        stringTranslationSet.add(createStringTranslation("téléphone", "fr"));

        qlString.setStringTranslation(stringTranslationSet);

        mockMvc.perform(put(AdminRestRouter.QlString.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qlString)))
                .andExpect(status().isOk());

        QlString qlStringSaved = qlStringRepository.findOne(updateQlString.getId());
        Set<StringTranslation> stringTranslationSavedSet = qlStringSaved.getStringTranslation();

        assertThat(stringTranslationSavedSet).hasSize(3);
        assertThat(containsTranslation("telefon", "mk", stringTranslationSavedSet)).isTrue();
        assertThat(containsTranslation("Telefon", "de", stringTranslationSavedSet)).isTrue();
        assertThat(containsTranslation("téléphone", "fr", stringTranslationSavedSet)).isTrue();
    }

    private StringTranslation createStringTranslation(String value, String languageCode) {
        StringTranslation stringTranslation = new StringTranslation();
        stringTranslation.setValue(value);
        stringTranslation.setLanguageCode(languageCode);
        stringTranslation.setStatus(Boolean.TRUE);
        return stringTranslation;
    }

    private boolean containsTranslation(String value, String languageCode, Set<StringTranslation> stringTranslationSavedSet) {
        for (StringTranslation stringTranslation : stringTranslationSavedSet) {
            if (stringTranslation.getLanguageCode().equals(languageCode)
                    && stringTranslation.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
