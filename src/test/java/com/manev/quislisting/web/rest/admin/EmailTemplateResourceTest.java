package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.EmailTemplate;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.domain.qlml.StringTranslation;
import com.manev.quislisting.repository.EmailTemplateRepository;
import com.manev.quislisting.service.EmailTemplateService;
import com.manev.quislisting.service.dto.EmailTemplateDTO;
import com.manev.quislisting.service.mapper.EmailTemplateMapper;
import com.manev.quislisting.web.rest.AdminRestRouter;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class EmailTemplateResourceTest {

    private static final String DEFAULT_NAME = "Default_Name";
    private static final String DEFAULT_TEXT = "Default_Text";
    private static final String DEFAULT_TEXT_BG = "Default_Text_BG";
    private static final String UPDATE_DEFAULT_NAME = "Update_Default_Name";
    private static final String UPDATE_DEFAULT_TEXT = "Update_DefaultText";
    private static final String UPDATE_DEFAULT_TEXT_BG = "Update_Default_Text_BG";

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private EmailTemplateMapper emailTemplateMapper;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    @Autowired

    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
    private EmailTemplate emailTemplate;
    private MockMvc restEmailNotificationMockMvc;

    public static EmailTemplate createEntity() {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setName(DEFAULT_NAME);
        emailTemplate.setText(DEFAULT_TEXT);

        return emailTemplate;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmailTemplateResource emailTemplateResource = new EmailTemplateResource(emailTemplateService);
        this.restEmailNotificationMockMvc = MockMvcBuilders.standaloneSetup(emailTemplateResource)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        emailTemplateRepository.deleteAll();
        emailTemplate = createEntity();
    }

    @Test
    @Transactional
    public void createEmailTemplate() throws Exception {
        int databaseSizeBeforeCrate = emailTemplateRepository.findAll().size();

        setDefaultQlString(emailTemplate);

        EmailTemplateDTO emailTemplateDTO = emailTemplateMapper.emailTemplateToEmailTemplateDTO(emailTemplate);
        restEmailNotificationMockMvc.perform(MockMvcRequestBuilders.post(AdminRestRouter.EmailTemplate.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emailTemplateDTO)))
                .andExpect(status().isCreated());

        List<EmailTemplate> emailTemplates = emailTemplateRepository.findAll();
        assertThat(emailTemplates).hasSize(databaseSizeBeforeCrate + 1);

        EmailTemplate emailTemplateSaved = emailTemplates.get(emailTemplates.size() - 1);
        assertThat(emailTemplateSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(emailTemplateSaved.getText()).isEqualTo(DEFAULT_TEXT);

        assertThat(emailTemplateSaved.getQlString().getValue()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void getEmailNotification() throws Exception {
        setDefaultQlString(emailTemplate);
        EmailTemplate emailTemplateSaved = emailTemplateRepository.saveAndFlush(emailTemplate);
        emailTemplateRepository.saveAndFlush(emailTemplateSaved);

        restEmailNotificationMockMvc.perform(get(AdminRestRouter.EmailTemplate.DETAIL,
                emailTemplateSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(emailTemplateSaved.getId()))
                .andExpect(jsonPath("$.name").value(emailTemplateSaved.getName()))
                .andExpect(jsonPath("$.text").value(emailTemplateSaved.getText()))
                .andExpect(jsonPath("$.qlString.value").value(emailTemplateSaved.getQlString().getValue()))
                .andExpect(jsonPath("$.qlString.stringTranslation[0].value")
                        .value(emailTemplateSaved.getQlString().getStringTranslation().iterator().next().getValue()));


    }

    private void setDefaultQlString(EmailTemplate emailTemplateSaved) {
        QlString qlString = new QlString();
        qlString.setValue(DEFAULT_TEXT);
        qlString.setLanguageCode("en");
        qlString.setName("email-template-#" + emailTemplateSaved.getName());
        qlString.setContext(EmailTemplateService.CONTEXT);
        qlString.setStatus(0);

        StringTranslation stringTranslationBG = new StringTranslation();
        stringTranslationBG.setLanguageCode("bg");
        stringTranslationBG.setValue(DEFAULT_TEXT_BG);
        stringTranslationBG.setQlString(qlString);
        stringTranslationBG.setStatus(Boolean.FALSE);
        stringTranslationBG.setTranslationDate(ZonedDateTime.now());

        qlString.addStringTranslation(stringTranslationBG);

        emailTemplateSaved.setQlString(qlString);
    }

    @Test
    @Transactional
    public void getNonExistingEmailNotification() throws Exception {
        restEmailNotificationMockMvc.perform(get(AdminRestRouter.EmailTemplate.DETAIL, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailNotification() throws Exception {
        setDefaultQlString(emailTemplate);
        emailTemplateRepository.saveAndFlush(emailTemplate);
        int databaseSizeBeforeUpdate = emailTemplateRepository.findAll().size();

        EmailTemplate updateEmailTemplate = emailTemplateRepository.findOne(this.emailTemplate.getId());
        updateEmailTemplate.setText(UPDATE_DEFAULT_TEXT);
        updateEmailTemplate.setName(UPDATE_DEFAULT_NAME);
        updateEmailTemplate.getQlString().setValue(UPDATE_DEFAULT_TEXT);
        updateEmailTemplate.getQlString().getStringTranslation().iterator().next().setValue(UPDATE_DEFAULT_TEXT_BG);

        EmailTemplateDTO updateEmailTemplateDTO = emailTemplateMapper.emailTemplateToEmailTemplateDTO(updateEmailTemplate);
        restEmailNotificationMockMvc.perform(put(AdminRestRouter.EmailTemplate.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateEmailTemplateDTO)))
                .andExpect(status().isOk());

        List<EmailTemplate> all = emailTemplateRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);
        EmailTemplate emailTemplateSaved = emailTemplateRepository.findOne(updateEmailTemplate.getId());
        assertThat(emailTemplateSaved.getName()).isEqualTo(UPDATE_DEFAULT_NAME);
        assertThat(emailTemplateSaved.getText()).isEqualTo(UPDATE_DEFAULT_TEXT);

        // verify also that qlString is created
        assertThat(emailTemplateSaved.getQlString().getValue()).isEqualTo(UPDATE_DEFAULT_TEXT);
        assertThat(emailTemplateSaved.getQlString().getStringTranslation().iterator().next().getValue()).isEqualTo(UPDATE_DEFAULT_TEXT_BG);
    }

    @Test
    @Transactional
    public void updateNoneExistingEmailNotification() throws Exception {
        setDefaultQlString(emailTemplate);
        int databaseSizeBeforeUpdate = emailTemplateRepository.findAll().size();
        EmailTemplateDTO emailTemplateDTO = emailTemplateMapper.emailTemplateToEmailTemplateDTO(emailTemplate);
        restEmailNotificationMockMvc.perform(put(AdminRestRouter.EmailTemplate.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emailTemplateDTO)))
                .andExpect(status().isCreated());

        List<EmailTemplate> all = emailTemplateRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate + 1);

    }

    @Test
    @Transactional
    public void deleteEmailNotification() throws Exception {
        setDefaultQlString(emailTemplate);
        emailTemplateRepository.saveAndFlush(emailTemplate);
        int databaseSizeBeforeDelete = emailTemplateRepository.findAll().size();

        restEmailNotificationMockMvc.perform(delete(AdminRestRouter.EmailTemplate.DETAIL,
                emailTemplate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<EmailTemplate> all = emailTemplateRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeDelete - 1);

    }

}
