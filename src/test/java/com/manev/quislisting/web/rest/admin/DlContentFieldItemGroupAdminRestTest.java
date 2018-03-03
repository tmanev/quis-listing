package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItemGroup;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.DlContentFieldItemGroupRepository;
import com.manev.quislisting.service.DlCategoryTestComponent;
import com.manev.quislisting.service.DlContentFieldItemGroupService;
import com.manev.quislisting.service.DlContentFieldTestComponent;
import com.manev.quislisting.service.dto.DlContentFieldItemGroupDTO;
import com.manev.quislisting.service.mapper.DlContentFieldItemGroupMapper;
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
public class DlContentFieldItemGroupAdminRestTest {

    private static final int DEFAULT_ORDER_NUM = 0;
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_DESCRIPTION = "DEFAULT_DESCRIPTION";

    private static final int UPDATED_ORDER_NUM = 1;
    private static final String UPDATED_NAME = "UPDATED_NAME";
    private static final String UPDATED_DESCRIPTION = "UPDATED_DESCRIPTION";

    private MockMvc mockMvc;

    @Autowired
    private DlContentFieldItemGroupService dlContentFieldItemGroupService;

    @Autowired
    private DlContentFieldItemGroupRepository dlContentFieldItemGroupRepository;

    @Autowired
    private DlContentFieldItemGroupMapper dlContentFieldItemGroupMapper;

    @Autowired
    private DlContentFieldTestComponent dlContentFieldTestComponent;

    @Autowired
    private DlCategoryTestComponent dlCategoryTestComponent;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private DlContentFieldItemGroup dlContentFieldItemGroup;

    public static DlContentFieldItemGroup createEntity() {
        DlContentFieldItemGroup group = new DlContentFieldItemGroup();
        group.setName(DEFAULT_NAME);
        group.setDescription(DEFAULT_DESCRIPTION);
        group.setOrderNum(DEFAULT_ORDER_NUM);
        return group;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DlContentFieldItemGroupAdminRest rest = new DlContentFieldItemGroupAdminRest(dlContentFieldItemGroupService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(rest)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter)
                .build();
    }

    @Before
    public void initTest() {
        dlContentFieldItemGroup = createEntity();
        DlCategory dlCategory = dlCategoryTestComponent.initCategory("en");
        DlContentField featuresCF = dlContentFieldTestComponent.createCheckboxField(dlCategory, "car-features");
        dlContentFieldItemGroup.setDlContentField(featuresCF);
    }

    @Test
    @Transactional
    public void createDlContentFieldItemGroup() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldItemGroupRepository.findAll().size();

        DlContentFieldItemGroupDTO dlContentFieldGroupDTO = dlContentFieldItemGroupMapper.mapDto(dlContentFieldItemGroup, null);

        mockMvc.perform(post(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST, dlContentFieldItemGroup.getDlContentField().getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldGroupDTO)))
                .andExpect(status().isCreated());

        // Validate the DlContentField in the database
        List<DlContentFieldItemGroup> dlContentFields = dlContentFieldItemGroupRepository.findAll();
        assertThat(dlContentFields).hasSize(databaseSizeBeforeCreate + 1);

        DlContentFieldItemGroup groupSaved = dlContentFields.get(dlContentFields.size() - 1);
        assertThat(groupSaved.getOrderNum()).isEqualTo(DEFAULT_ORDER_NUM);
        assertThat(groupSaved.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(groupSaved.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDlContentFieldItemGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dlContentFieldItemGroupRepository.findAll().size();

        DlContentFieldItemGroup dlContentFieldItemGroup = new DlContentFieldItemGroup();
        dlContentFieldItemGroup.setId(1L);
        DlContentFieldItemGroupDTO existingDlContentFieldGroupDTO = dlContentFieldItemGroupMapper.mapDto(dlContentFieldItemGroup, null);

        // An entity with an existing ID cannot be created, so this API call must fail
        mockMvc.perform(post(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST, this.dlContentFieldItemGroup.getDlContentField().getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingDlContentFieldGroupDTO)))
                .andExpect(status().isBadRequest());

        // Validate the group in the database
        List<DlContentFieldItemGroup> groups = dlContentFieldItemGroupRepository.findAll();
        assertThat(groups).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getDlContentFieldItemGroup() throws Exception {
        // Initialize the database
        dlContentFieldItemGroupRepository.saveAndFlush(dlContentFieldItemGroup);

        // Get the DlContentFieldGroup
        mockMvc.perform(get(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.DETAIL,
                dlContentFieldItemGroup.getDlContentField().getId(),
                dlContentFieldItemGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(dlContentFieldItemGroup.getId().intValue()))
                .andExpect(jsonPath("$.orderNum").value(dlContentFieldItemGroup.getOrderNum()))
                .andExpect(jsonPath("$.name").value(dlContentFieldItemGroup.getName()))
                .andExpect(jsonPath("$.description").value(dlContentFieldItemGroup.getDescription()));
    }

    @Test
    @Transactional
    public void getNonExistingDlContentFieldItemGroup() throws Exception {
        // Get non existing DlContentFieldGroup
        mockMvc.perform(get(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.DETAIL,
                dlContentFieldItemGroup.getDlContentField().getId(),
                Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDlContentFieldItemGroup() throws Exception {
        // Initialize the database
        dlContentFieldItemGroupRepository.saveAndFlush(dlContentFieldItemGroup);
        int databaseSizeBeforeUpdate = dlContentFieldItemGroupRepository.findAll().size();

        // Update the group
        DlContentFieldItemGroup updatedDlContentFieldGroup = dlContentFieldItemGroupRepository.findOne(this.dlContentFieldItemGroup.getId());
        updatedDlContentFieldGroup.setOrderNum(UPDATED_ORDER_NUM);
        updatedDlContentFieldGroup.setName(UPDATED_NAME);
        updatedDlContentFieldGroup.setDescription(UPDATED_DESCRIPTION);

        DlContentFieldItemGroupDTO updateContentFieldDTO = dlContentFieldItemGroupMapper.mapDto(updatedDlContentFieldGroup, null);

        mockMvc.perform(put(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST, dlContentFieldItemGroup.getDlContentField().getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateContentFieldDTO)))
                .andExpect(status().isOk());

        // Validate the DlContentField in the database
        List<DlContentFieldItemGroup> all = dlContentFieldItemGroupRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate);

        DlContentFieldItemGroup dlContentFieldSaved = dlContentFieldItemGroupRepository.findOne(updatedDlContentFieldGroup.getId());
        assertThat(dlContentFieldSaved.getOrderNum()).isEqualTo(UPDATED_ORDER_NUM);
        assertThat(dlContentFieldSaved.getName()).isEqualTo(UPDATED_NAME);
        assertThat(dlContentFieldSaved.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDlContentFieldItemGroup() throws Exception {
        int databaseSizeBeforeUpdate = dlContentFieldItemGroupRepository.findAll().size();

        // Create the DTO
        DlContentFieldItemGroupDTO dlContentFieldItemGroupDTO = dlContentFieldItemGroupMapper.mapDto(dlContentFieldItemGroup, null);

        // If the entity does not have an ID, it will be created instead of just being updated
        mockMvc.perform(put(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.LIST, dlContentFieldItemGroup.getDlContentField().getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dlContentFieldItemGroupDTO)))
                .andExpect(status().isCreated());

        // Validate the DlContentField in the database
        List<DlContentFieldItemGroup> all = dlContentFieldItemGroupRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDlContentFieldItemGroup() throws Exception {
        // Initialize the database
        dlContentFieldItemGroupRepository.saveAndFlush(dlContentFieldItemGroup);
        int databaseSizeBeforeDelete = dlContentFieldItemGroupRepository.findAll().size();

        // Delete the DlContentFieldGroup
        mockMvc.perform(delete(AdminRestRouter.DlContentFields.DlContentFieldItemGroup.DETAIL,
                dlContentFieldItemGroup.getDlContentField().getId(),
                dlContentFieldItemGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DlContentFieldItemGroup> all = dlContentFieldItemGroupRepository.findAll();
        assertThat(all).hasSize(databaseSizeBeforeDelete - 1);
    }

}