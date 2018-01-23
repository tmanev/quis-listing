package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.DlContentFieldItemRepository;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.service.DlContentFieldItemService;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.service.mapper.DlContentFieldItemMapper;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class DlContentFieldItemAdminRestTest {

    private static final String DEFAULT_VALUE = "DEFAULT_VALUE";
    private static final String UPDATE_VALUE = "UPDATE_VALUE";
    private static final Integer DEFAULT_ORDER_NUM = 0;
    private static final Integer UPDATE_ORDER_NUM = 1;

    @Autowired
    private DlContentFieldItemService dlContentFieldItemService;

    @Autowired
    private DlContentFieldItemRepository dlContentFieldItemRepository;

    @Autowired
    private DlContentFieldRepository dlContentFieldRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private DlContentFieldItemMapper dlContentFieldItemMapper;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDlContentFieldItemMockMvc;

    private DlContentFieldItem dlContentFieldItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlContentFieldItemAdminRest dlContentFieldItemResource = new DlContentFieldItemAdminRest(dlContentFieldItemService);
        this.restDlContentFieldItemMockMvc = MockMvcBuilders.standaloneSetup(dlContentFieldItemResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        dlContentFieldItem = createEntity();
    }

    private DlContentFieldItem createEntity() {
        return new DlContentFieldItem()
                .value(DEFAULT_VALUE)
                .qlString(new QlString().languageCode("en").context("dl-content-field-item").name("dl-content-field-#some_id").value(DEFAULT_VALUE).status(0))
                .orderNum(DEFAULT_ORDER_NUM);
    }

    @Test
    @Transactional
    public void createDlContentFieldItem() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldItemRepository.findAll().size();

        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.save(dlContentField);

        DlContentFieldItemDTO dlContentFieldItemDTO = dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, null);
        restDlContentFieldItemMockMvc.perform(MockMvcRequestBuilders.post(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST.replace("{dlContentFieldId}", dlContentField.getId().toString()))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldItemDTO)))
                .andExpect(status().isCreated());

        List<DlContentFieldItem> all = dlContentFieldItemRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeCreate + 1);

        DlContentFieldItem dlContentFieldItemSaved = all.get(all.size() - 1);
        assertThat(dlContentFieldItemSaved.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(dlContentFieldItemSaved.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
    }

    @Test
    @Transactional
    public void createDlContentFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldItemRepository.findAll().size();

        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.save(dlContentField);

        DlContentFieldItem existingDlContentFieldItem = new DlContentFieldItem();
        existingDlContentFieldItem.setId(1L);
        DlContentFieldItemDTO existingDlContentFieldItemDTO = dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(existingDlContentFieldItem, null);

        restDlContentFieldItemMockMvc.perform(post(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST.replace("{dlContentFieldId}", dlContentField.getId().toString()))
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlContentFieldItemDTO)))
                .andExpect(status().isBadRequest());

        // Validate the DlContentField in the database
        List<DlContentFieldItem> dlContentFieldItems = dlContentFieldItemRepository.findAll();
        assertThat(dlContentFieldItems).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getDlContentFieldItem() throws Exception {
        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.save(dlContentField);

        dlContentFieldItem.setDlContentField(dlContentField);
        dlContentFieldItemRepository.saveAndFlush(dlContentFieldItem);

        restDlContentFieldItemMockMvc.perform(get(AdminRestRouter.DlContentFields.DlContentFieldItem.DETAIL, dlContentField.getId(), dlContentFieldItem.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlContentFieldItem.getId().intValue()))
                .andExpect(jsonPath("$.value").value(dlContentFieldItem.getValue()))
                .andExpect(jsonPath("$.orderNum").value(dlContentFieldItem.getOrderNum()));
    }

    @Test
    @Transactional
    public void getNonExistingDlContentFieldItem() throws Exception {
        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.save(dlContentField);

        restDlContentFieldItemMockMvc.perform(get(AdminRestRouter.DlContentFields.DlContentFieldItem.DETAIL, dlContentField.getId(), Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlContentFieldItem() throws Exception {
        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.saveAndFlush(dlContentField);

        dlContentFieldItem.setDlContentField(dlContentField);
        dlContentFieldItemRepository.saveAndFlush(dlContentFieldItem);

        int databaseSizeBeforeUpdate = dlContentFieldItemRepository.findAll().size();

        DlContentFieldItem updateDlContentFieldItem = dlContentFieldItemRepository.findOne(dlContentFieldItem.getId());
        updateDlContentFieldItem.value(UPDATE_VALUE).orderNum(UPDATE_ORDER_NUM);

        DlContentFieldItemDTO updateDlContentFieldItemDTO = dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(updateDlContentFieldItem, null);

        restDlContentFieldItemMockMvc.perform(put(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST, dlContentField.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateDlContentFieldItemDTO)))
                .andExpect(status().isOk());

        List<DlContentFieldItem> all = dlContentFieldItemRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);

        DlContentFieldItem dlContentFieldItemSaved = dlContentFieldItemRepository.findOne(updateDlContentFieldItem.getId());
        assertThat(dlContentFieldItemSaved.getValue()).isEqualTo(UPDATE_VALUE);
        assertThat(dlContentFieldItemSaved.getOrderNum()).isEqualTo(UPDATE_ORDER_NUM);
    }

    @Test
    @Transactional
    public void updateNonExistingDlContentFieldItem() throws Exception {
        int databaseSizeBeforeUpdate = dlContentFieldItemRepository.findAll().size();

        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.saveAndFlush(dlContentField);

        DlContentFieldItemDTO dlContentFieldItemDTO = dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, null);
        restDlContentFieldItemMockMvc.perform(put(AdminRestRouter.DlContentFields.DlContentFieldItem.LIST, dlContentField.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldItemDTO)))
                .andExpect(status().isCreated());

        List<DlContentFieldItem> all = dlContentFieldItemRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContentFieldItem() throws Exception {
        DlContentField dlContentField = DlContentFieldAdminRestTest.createEntity();
        dlContentFieldRepository.saveAndFlush(dlContentField);

        dlContentFieldItem.setDlContentField(dlContentField);
        dlContentFieldItemRepository.saveAndFlush(dlContentFieldItem);

        int databaseSizeBeforeDelete = dlContentFieldItemRepository.findAll().size();

        restDlContentFieldItemMockMvc.perform(delete(AdminRestRouter.DlContentFields.DlContentFieldItem.DETAIL,
                dlContentField.getId(), dlContentFieldItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlContentFieldItem> all = dlContentFieldItemRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeDelete - 1);
    }
}