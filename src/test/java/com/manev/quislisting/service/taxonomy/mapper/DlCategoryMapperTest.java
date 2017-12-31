package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.TranslationBuilder;
import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.service.post.mapper.TranslationMapper;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class DlCategoryMapperTest {

    private DlCategoryMapper dlCategoryMapper;

    @Before
    public void setUp() {
        dlCategoryMapper = new DlCategoryMapper(new TranslationMapper());
    }

    private DlCategory createDlCategory(Long id, DlCategory parent) {
        TranslationGroup translationGroup = new TranslationGroup();
        translationGroup.setTranslations(new HashSet<>());
        return DlCategoryBuilder.aDlCategory()
                .withId(id)
                .withParent(parent)
                .withTranslation(TranslationBuilder.aTranslation()
                        .withLanguageCode("en")
                        .withTranslationGroup(translationGroup)
                        .build())
                .build();
    }

    @Test
    public void dlCategoryToDlCategoryDtoFlat() throws Exception {
        List<DlCategory> dlCategoryList = new ArrayList<>();
        DlCategory dlCat1 = createDlCategory(1L, null);
        dlCategoryList.add(dlCat1);
        dlCategoryList.add(createDlCategory(2L, null));
        dlCategoryList.add(createDlCategory(3L, dlCat1));
        DlCategory dlCat4 = createDlCategory(4L, dlCat1);
        dlCategoryList.add(dlCat4);
        dlCategoryList.add(createDlCategory(5L, dlCat1));
        DlCategory dlCat6 = createDlCategory(6L, dlCat1);
        dlCategoryList.add(dlCat6);
        dlCategoryList.add(createDlCategory(7L, dlCat4));
        dlCategoryList.add(createDlCategory(8L, dlCat4));
        DlCategory dlCat9 = createDlCategory(9L, dlCat6);
        dlCategoryList.add(dlCat9);
        DlCategory dlCat10 = createDlCategory(10L, dlCat9);
        dlCategoryList.add(dlCat10);
        dlCategoryList.add(createDlCategory(11L, dlCat9));
        dlCategoryList.add(createDlCategory(12L, dlCat10));

        PageImpl<DlCategory> page = new PageImpl<>(dlCategoryList);

        List<DlCategoryDTO> dlCategoryDTOs = dlCategoryMapper.dlCategoryToDlCategoryDtoFlat(page.getContent());
        assertEquals(dlCategoryList.size(), dlCategoryDTOs.size());
        assertEquals(0, findById(1L, dlCategoryDTOs).getDepthLevel());
        assertEquals(0, findById(2L, dlCategoryDTOs).getDepthLevel());
        assertEquals(1, findById(3L, dlCategoryDTOs).getDepthLevel());
        assertEquals(1, findById(4L, dlCategoryDTOs).getDepthLevel());
        assertEquals(1, findById(5L, dlCategoryDTOs).getDepthLevel());
        assertEquals(1, findById(6L, dlCategoryDTOs).getDepthLevel());
        assertEquals(2, findById(7L, dlCategoryDTOs).getDepthLevel());
        assertEquals(2, findById(8L, dlCategoryDTOs).getDepthLevel());
        assertEquals(2, findById(9L, dlCategoryDTOs).getDepthLevel());
        assertEquals(3, findById(10L, dlCategoryDTOs).getDepthLevel());
        assertEquals(3, findById(11L, dlCategoryDTOs).getDepthLevel());
        assertEquals(4, findById(12L, dlCategoryDTOs).getDepthLevel());
    }

    private DlCategoryDTO findById(Long id, List<DlCategoryDTO> dlCategoryDTOList) {
        for (DlCategoryDTO dlCategoryDTO : dlCategoryDTOList) {
            if (dlCategoryDTO.getId().equals(id)) {
                return dlCategoryDTO;
            }
        }
        return null;
    }

}