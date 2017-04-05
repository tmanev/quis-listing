package com.manev.quislisting.web.rest;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.EmailNotification;
import com.manev.quislisting.repository.EmailNotificationRepository;
import com.manev.quislisting.service.EmailNotificationService;
import com.manev.quislisting.service.dto.EmailNotificationDTO;
import com.manev.quislisting.service.mapper.EmailNotificationMapper;
import org.apache.catalina.servlet4preview.http.Mapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_EMAIL_NOTIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.awt.print.Pageable;
import java.util.List;

/**
 * Created by adri on 4/5/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class EmailNotificationIntTest {


    public static final String DEFAULT_NAME = "Default_Name";
    public static final String DEFAULT_TEXT = "Default_Text";
    public static final String UPDATE_DEFAULT_NAME= "Update_Default_Name";
    public static final String UPDATE_DIFAULT_TEXT= "Update_DefaultText";

    @Autowired
    private EmailNotificationRepository emailNotificationRepository;
    @Autowired
    private EmailNotificationMapper emailNotificationMapper;
    @Autowired
    private EmailNotificationService emailNotificationService;
    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    @Autowired

    private PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver;
    private EmailNotification emailNotification;
    private MockMvc restEmailNotificationMockMvc;

    public static EmailNotification createEntity(){
       EmailNotification emailNotification =new EmailNotification();
       emailNotification.setName(DEFAULT_NAME);
       emailNotification.setText(DEFAULT_TEXT);
       return emailNotification;

    }



    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        EmailNotificationResource emailNotificationResource = new EmailNotificationResource(emailNotificationService);
        this.restEmailNotificationMockMvc= MockMvcBuilders.standaloneSetup(emailNotificationResource)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();
    }

    @Before
    public void initTest(){
        emailNotificationRepository.deleteAll();
        emailNotification=createEntity();
    }



    @Test
    @Transactional
    public void createEmailNotification() throws Exception{
        int databaseSizeBeforeCrate=emailNotificationRepository.findAll().size();

        EmailNotificationDTO emailNotificationDTO = emailNotificationMapper.emailNotificationToEmailNotificationDTO(emailNotification);
        restEmailNotificationMockMvc.perform(post(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emailNotificationDTO)))
                .andExpect(status().isCreated());

        List<EmailNotification> emailNotifications = emailNotificationRepository.findAll();
        assertThat(emailNotifications).hasSize(databaseSizeBeforeCrate+1);

        EmailNotification emailNotificationSaved = emailNotifications.get(emailNotifications.size()-1);
        assertThat(emailNotificationSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(emailNotificationSaved.getText()).isEqualTo(DEFAULT_TEXT);

    }

    @Test
    @Transactional
    public void getEmailNotification() throws Exception{
        EmailNotification emailNotificationSaved = emailNotificationRepository.saveAndFlush(emailNotification);
        emailNotificationRepository.saveAndFlush(emailNotification);

        restEmailNotificationMockMvc.perform(get(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION + "/{id}",
                emailNotification.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(emailNotification.getId()))
                .andExpect(jsonPath("$.name").value(emailNotification.getName()))
                .andExpect(jsonPath("$.text").value(emailNotification.getText()));

    }

    @Test
    @Transactional
    public void getNonExistingEmailNotification() throws Exception{
        restEmailNotificationMockMvc.perform(get(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public  void updateEmailNotification() throws  Exception{
        emailNotificationRepository.saveAndFlush(emailNotification);
        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();

        EmailNotification updateEmailNotification = emailNotificationRepository.findOne(this.emailNotification.getId());
        updateEmailNotification.setText("UPDATE_DEFAULT_TEXT");
        updateEmailNotification.setName("UPDATE_DEAFAULT_NAME");

        EmailNotificationDTO updateEmailNotificationDTO = emailNotificationMapper.emailNotificationToEmailNotificationDTO(updateEmailNotification);
        restEmailNotificationMockMvc.perform(put(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateEmailNotification)))
                .andExpect(status().isOk());

        List<EmailNotification> all = emailNotificationRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);
        EmailNotification emailNotificationSaved = emailNotificationRepository.findOne(updateEmailNotification.getId());
        assertThat(emailNotificationSaved.getName()).isNotEqualTo(UPDATE_DEFAULT_NAME);
        assertThat(emailNotificationSaved.getText()).isNotEqualTo(UPDATE_DIFAULT_TEXT);

    }

    @Test
    @Transactional
    public  void updateNoneExistingEmailNotification() throws Exception{

        int databaseSizeBeforeUpdate = emailNotificationRepository.findAll().size();
        EmailNotificationDTO emailNotificationDTO = emailNotificationMapper.emailNotificationToEmailNotificationDTO(emailNotification);
        restEmailNotificationMockMvc.perform(put(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(emailNotificationDTO)))
                .andExpect(status().isCreated());

        List<EmailNotification>all =emailNotificationRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate +1);

    }


    @Test
    @Transactional
    public void deleteEmailNotification() throws Exception{
        emailNotificationRepository.saveAndFlush(emailNotification);
        int databaseSizeBeforeDelete = emailNotificationRepository.findAll().size();

        restEmailNotificationMockMvc.perform(delete(RESOURCE_API_ADMIN_EMAIL_NOTIFICATION + "/{id}",
                emailNotification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<EmailNotification> all = emailNotificationRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeDelete -1);

    }







}
