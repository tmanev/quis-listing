package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentFieldGroup;
import com.manev.quislisting.repository.DlContentFieldGroupRepository;
import com.manev.quislisting.service.DlContentFieldGroupService;
import com.manev.quislisting.service.dto.DlContentFieldGroupDTO;
import com.manev.quislisting.service.mapper.DlContentFieldGroupMapper;
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
public class DlContentFieldGroupAdminRestTest {

    private static final int DEFAULT_ORDER_NUM = 0;
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_SLUG = "DEFAULT_SLUG";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";

    private static final int UPDATED_ORDER_NUM = 1;
    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_SLUG = "UPDATED_SLUG";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";

    private MockMvc mockMvc;

    @Autowired
    private DlContentFieldGroupService dlContentFieldGroupService;

    @Autowired
    private DlContentFieldGroupRepository dlContentFieldGroupRepository;

    @Autowired
    private DlContentFieldGroupMapper dlContentFieldGroupMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private DlContentFieldGroup dlContentFieldGroup;

    public static DlContentFieldGroup createEntity() {
        DlContentFieldGroup dlContentFieldGroup = new DlContentFieldGroup();
        dlContentFieldGroup.setName(DEFAULT_NAME);
        dlContentFieldGroup.setSlug(DEFAULT_SLUG);
        dlContentFieldGroup.setDescription(DEFAULT_DESCRIPTION);
        dlContentFieldGroup.setOrderNum(DEFAULT_ORDER_NUM);
        return dlContentFieldGroup;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlContentFieldGroupAdminRest dlContentFieldGroupResource = new DlContentFieldGroupAdminRest(dlContentFieldGroupService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dlContentFieldGroupResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        dlContentFieldGroup = createEntity();
    }

    @Test
    @Transactional
    public void createDlContentFieldGroup() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldGroupRepository.findAll().size();

        DlContentFieldGroupDTO dlContentFieldGroupDTO = dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentFieldGroup);

        mockMvc.perform(post(AdminRestRouter.DlContentFieldGroup.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldGroupDTO)))
                .andExpect(status().isCreated());

        // Validate the DlContentField in the database
        List<DlContentFieldGroup> dlContentFields = dlContentFieldGroupRepository.findAll();
        assertThat(dlContentFields).hasSize(databaseSizeBeforeCreate + 1);

        DlContentFieldGroup dlContentFieldGroupSaved = dlContentFields.get(dlContentFields.size() - 1);
        assertThat(dlContentFieldGroupSaved.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(dlContentFieldGroupSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(dlContentFieldGroupSaved.getSlug()).isEqualTo(DEFAULT_SLUG);
        assertThat(dlContentFieldGroupSaved.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDlContentFieldGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldGroupRepository.findAll().size();

        DlContentFieldGroup dlContentFieldGroup = new DlContentFieldGroup();
        dlContentFieldGroup.setId(1L);
        DlContentFieldGroupDTO existingDlContentFieldGroupDTO = dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentFieldGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        mockMvc.perform(post(AdminRestRouter.DlContentFieldGroup.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlContentFieldGroupDTO)))
                .andExpect(status().isBadRequest());

        // Validate the DlContentFieldGroup in the database
        List<DlContentFieldGroup> dlContentFieldGroups = dlContentFieldGroupRepository.findAll();
        assertThat(dlContentFieldGroups).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getDlContentFieldGroup() throws Exception {
        // Initialize the database
        dlContentFieldGroupRepository.saveAndFlush(dlContentFieldGroup);

        // Get the DlContentFieldGroup
        mockMvc.perform(get(AdminRestRouter.DlContentFieldGroup.DETAIL,
                dlContentFieldGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlContentFieldGroup.getId().intValue()))
                .andExpect(jsonPath("$.orderNum").value(dlContentFieldGroup.getOrderNum()))
                .andExpect(jsonPath("$.name").value(dlContentFieldGroup.getName()))
                .andExpect(jsonPath("$.slug").value(dlContentFieldGroup.getSlug()))
                .andExpect(jsonPath("$.description").value(dlContentFieldGroup.getDescription()))
        ;
    }

    @Test
    @Transactional
    public void getNonExistingDlContentFieldGroup() throws Exception {
        // Get non existing DlContentFieldGroup
        mockMvc.perform(get(AdminRestRouter.DlContentFieldGroup.DETAIL, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlContentFieldGroup() throws Exception {
        // Initialize the database
        dlContentFieldGroupRepository.saveAndFlush(dlContentFieldGroup);
        int databaseSizeBeforeUpdate = dlContentFieldGroupRepository.findAll().size();

        // Update the DlContentFieldGroup
        DlContentFieldGroup updatedDlContentFieldGroup = dlContentFieldGroupRepository.findOne(this.dlContentFieldGroup.getId());
        updatedDlContentFieldGroup.setOrderNum(UPDATED_ORDER_NUM);
        updatedDlContentFieldGroup.setName(UPDATED_NAME);
        updatedDlContentFieldGroup.setSlug(UPDATED_SLUG);
        updatedDlContentFieldGroup.setDescription(UPDATED_DESCRIPTION);

        DlContentFieldGroupDTO updateContentFieldDTO = dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(updatedDlContentFieldGroup);

        mockMvc.perform(put(AdminRestRouter.DlContentFieldGroup.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateContentFieldDTO)))
                .andExpect(status().isOk());

        // Validate the DlContentField in the database
        List<DlContentFieldGroup> all = dlContentFieldGroupRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);

        DlContentFieldGroup dlContentFieldSaved = dlContentFieldGroupRepository.findOne(updatedDlContentFieldGroup.getId());
        assertThat(dlContentFieldSaved.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(dlContentFieldSaved.getName()).isEqualTo(UPDATED_NAME);
        assertThat(dlContentFieldSaved.getSlug()).isEqualTo(UPDATED_SLUG);
        assertThat(dlContentFieldSaved.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDlContentFieldGroup() throws Exception {
        int databaseSizeBeforeUpdate = dlContentFieldGroupRepository.findAll().size();

        // Create the DTO
        DlContentFieldGroupDTO dlContentFieldGroupDTO = dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentFieldGroup);

        // If the entity does not have an ID, it will be created instead of just being updated
        mockMvc.perform(put(AdminRestRouter.DlContentFieldGroup.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldGroupDTO)))
                .andExpect(status().isCreated());

        // Validate the DlContentField in the database
        List<DlContentFieldGroup> all = dlContentFieldGroupRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDlContentFieldGroup() throws Exception {
        // Initialize the database
        dlContentFieldGroupRepository.saveAndFlush(dlContentFieldGroup);
        int databaseSizeBeforeDelete = dlContentFieldGroupRepository.findAll().size();

        // Delete the DlContentFieldGroup
        mockMvc.perform(delete(AdminRestRouter.DlContentFieldGroup.DETAIL,
                dlContentFieldGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlContentFieldGroup> all = dlContentFieldGroupRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeDelete - 1);
    }

}