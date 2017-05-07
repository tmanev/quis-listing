package com.manev.quislisting.web.rest.qlml;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.EmailNotification;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.qlml.QlStringRepository;
import com.manev.quislisting.service.dto.EmailNotificationDTO;
import com.manev.quislisting.service.qlml.QlStringService;
import com.manev.quislisting.web.rest.TestUtil;
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
import java.util.List;
import java.util.Set;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_EMAIL_NOTIFICATION;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_QL_STRINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class QlStringTest {


    private static final String DEFAULT_LCODE = "en";
    private static final String DEFAULT_CONTEXT = "default context";
    private static final String DEFAULT_NAME = "default name";
    private static final String DEFAULT_VALUE = "default value";
    private static final String DEFAULT_TYPE = "default type";
    private static final Integer DEFAULT_STATUS = 0;


    @Autowired
    private QlStringResource qlStringResource;
    @Autowired
    private QlStringRepository qlStringRepository;

    @Autowired
    private QlStringService qlStringService;
    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    private MockMvc restQlStringMockMvc;

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
        this.restQlStringMockMvc = MockMvcBuilders.standaloneSetup(qlStringResource)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        qlStringRepository.deleteAll();
        qlString = createEntity();
    }

    //
//
//    @Before
//    public void initTest(){
//        emailNotificationRepository.deleteAll();
//        emailNotification=createEntity();
//    }
    @Test
    @Transactional
    public void getQlString() throws Exception {
        qlStringRepository.saveAndFlush(qlString);

        restQlStringMockMvc.perform(get(RESOURCE_API_ADMIN_QL_STRINGS + "/{id}", qlString.getId()))
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
        restQlStringMockMvc.perform(get(RESOURCE_API_ADMIN_QL_STRINGS + "/{id}", Long.MAX_VALUE))
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

        restQlStringMockMvc.perform(put(RESOURCE_API_ADMIN_QL_STRINGS)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qlString)))
                .andExpect(status().isOk());

        QlString qlStringSaved = qlStringRepository.findOne(updateQlString.getId());
        Set<StringTranslation> stringTranslationSavedSet = qlStringSaved.getStringTranslation();

        assertThat(stringTranslationSavedSet).hasSize(3);
        assertThat(containsTranslation("telefon", "mk", stringTranslationSavedSet)).isTrue();
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
                    && stringTranslation.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }


//
//        List<EmailNotification> all = emailNotificationRepository.findAll();
//        assertThat(all).hasSize(databaseSizeBeforeUpdate);
//        EmailNotification emailNotificationSaved = emailNotificationRepository.findOne(updateEmailNotification.getId());
//        assertThat(emailNotificationSaved.getName()).isNotEqualTo(UPDATE_DEFAULT_NAME);
//        assertThat(emailNotificationSaved.getText()).isNotEqualTo(UPDATE_DIFAULT_TEXT);
//
//    }
//
//    @Test
//    @Transactional
//    public  void updateNoneExistingEmailNotification() throws Exception{
//
//        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
//        EmailNotificationDTO emailNotificationDTO = emailNotificationMapper.emailNotificationToEmailNotificationDTO(emailNotification);
//        restEmailNotificationMockMvc.perform(put(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION)
//                .contentType(TestUtil.APPLICATION_JSON_UTF8)
//                .content(TestUtil.convertObjectToJsonBytes(emailNotificationDTO)))
//                .andExpect(status().isCreated());
//
//        List<EmailNotification>all =emailNotificationRepository.findAll();
//        assertThat(all).hasSize(databaseSizeBeforeUpdate +1);
//
//    }


}
