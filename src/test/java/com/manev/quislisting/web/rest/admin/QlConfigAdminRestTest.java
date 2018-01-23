package com.manev.quislisting.web.rest.admin;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.repository.QlConfigRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.dto.QlConfigDTO;
import com.manev.quislisting.service.mapper.QlConfigMapper;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Стефан on 05.04.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class QlConfigAdminRestTest {

    public static final String DEFAULT_KEY = "DEFAULT_KEY";
    public static final String DEFAULT_NAME = "DEFAULT_NAME";

    public static final String UPDATED_KEY = "UPDATED_KEY";
    public static final String UPDATED_VALUE = "UPDATED_VALUE";

    @Autowired
    private QlConfigRepository qlConfigRepository;

    @Autowired
    private QlConfigMapper qlConfigMapper;

    @Autowired
    private QlConfigService qlConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restQlConfigMockMvc;

    private QlConfig qlConfig;


    public static QlConfig createEntity(){

        QlConfig qlConfig = new QlConfig();
        qlConfig.setQlKey(DEFAULT_KEY);
        qlConfig.setValue(DEFAULT_NAME);
        return qlConfig;

    }

    @Before
    public void initTest() {
        qlConfigRepository.deleteAll();
        qlConfig = createEntity();
    }

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        QlConfigAdminRest qlConfigResource = new QlConfigAdminRest(qlConfigService);
        this.restQlConfigMockMvc = MockMvcBuilders.standaloneSetup(qlConfigResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter)
                .build();

    }

    @Test
    @Transactional
    public void createQlConfig() throws Exception{
        int databaseSizeBeforeCreate = qlConfigRepository.findAll().size();

        QlConfigDTO qlConfigDTO = qlConfigMapper.qlConfigToQlConfigDTO(qlConfig);

        restQlConfigMockMvc.perform(MockMvcRequestBuilders.post(AdminRestRouter.QlConfig.LIST)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(qlConfigDTO)))
                .andExpect(status().isCreated());


        List<QlConfig> qlConfigs = qlConfigRepository.findAll();
        assertThat(qlConfigs).hasSize(databaseSizeBeforeCreate + 1);

        QlConfig qlConfigSaved = qlConfigs.get(qlConfigs.size() - 1);
        assertThat(qlConfigSaved.getQlKey()).isEqualTo(DEFAULT_KEY);
        assertThat(qlConfigSaved.getValue()).isEqualTo(DEFAULT_NAME);


    }

    @Test
    @Transactional
    public void getQlConfig() throws Exception{

        qlConfigRepository.saveAndFlush(qlConfig);

        restQlConfigMockMvc.perform(get(AdminRestRouter.QlConfig.DETAIL,
                qlConfig.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(qlConfig.getId().intValue()))
                .andExpect(jsonPath("$.qlKey").value(qlConfig.getQlKey()))
                .andExpect(jsonPath("$.value").value(qlConfig.getValue()));

    }

    @Test
    @Transactional
    public void getNonExistingQlConfig() throws Exception{
        restQlConfigMockMvc.perform(get(AdminRestRouter.QlConfig.DETAIL, Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQlConfig() throws Exception{

        qlConfigRepository.saveAndFlush(qlConfig);
        int databaseBeforeUpdate = qlConfigRepository.findAll().size();

        QlConfig updatedQlConfig = qlConfigRepository.findOne(this.qlConfig.getId());

        updatedQlConfig.setQlKey(UPDATED_KEY);
        updatedQlConfig.setValue(UPDATED_VALUE);

        QlConfigDTO updateQlConfigDTO = qlConfigMapper.qlConfigToQlConfigDTO(updatedQlConfig);

        restQlConfigMockMvc.perform(put(AdminRestRouter.QlConfig.LIST)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updateQlConfigDTO)))
                .andExpect(status().isOk());

        List<QlConfig> all = qlConfigRepository.findAll();
        assertThat(all).hasSize(databaseBeforeUpdate);

        QlConfig qlConfigSaved = qlConfigRepository.findOne(updatedQlConfig.getId());
        assertThat(qlConfigSaved.getQlKey()).isEqualTo(UPDATED_KEY);
        assertThat(qlConfigSaved.getValue()).isEqualTo(UPDATED_VALUE);

    }

    @Test
    @Transactional
    public void updateNonExistingQlConfig() throws Exception {

        int databaseBeforeUpdate = qlConfigRepository.findAll().size();

        QlConfigDTO qlConfigDTO = qlConfigMapper.qlConfigToQlConfigDTO(qlConfig);

        restQlConfigMockMvc.perform(put(AdminRestRouter.QlConfig.LIST)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qlConfigDTO)))
            .andExpect(status().isCreated());

        List<QlConfig> all = qlConfigRepository.findAll();
        assertThat(all).hasSize(databaseBeforeUpdate + 1);

    }

    @Test
    @Transactional
    public void deleteQlConfig() throws Exception {

        qlConfigRepository.saveAndFlush(qlConfig);
        int databaseBeforeDelete = qlConfigRepository.findAll().size();

        restQlConfigMockMvc.perform(delete(AdminRestRouter.QlConfig.DETAIL,
                qlConfig.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<QlConfig> all = qlConfigRepository.findAll();
        assertThat(all).hasSize(databaseBeforeDelete -1);

    }
}
