package com.manev.quislisting.web.rest.qlml;

import com.manev.QuisListingApp;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.qlml.QlStringRepository;
import com.manev.quislisting.service.qlml.QlStringService;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_QL_STRINGS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QuisListingApp.class)
public class QlStringTest {



    private static final String DEFAULT_LCODE ="en";
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

    public static QlString createEntity(){
        QlString qlString =new QlString();
        qlString.setName(DEFAULT_NAME);
        qlString.setValue(DEFAULT_VALUE);
        qlString.setContext(DEFAULT_CONTEXT);
        qlString.setLanguageCode(DEFAULT_LCODE);
        qlString.setStatus(DEFAULT_STATUS);

        return qlString;

    }


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        QlStringResource qlStringResource = new QlStringResource(qlStringService);
        this.restQlStringMockMvc=MockMvcBuilders.standaloneSetup(qlStringResource)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .setMessageConverters(jackson2HttpMessageConverter)
                .build();
    }

    @Before
    public void initTest(){
        qlStringRepository.deleteAll();
        qlString=createEntity();
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
    public void getQlString() throws Exception{
        qlStringRepository.saveAndFlush(qlString);

        restQlStringMockMvc.perform(get(RESOURCE_API_ADMIN_QL_STRINGS +"/{id}", qlString.getId()))
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
    public void getNonExistingQlString() throws Exception{
        restQlStringMockMvc.perform(get(RESOURCE_API_ADMIN_QL_STRINGS + "/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

}
