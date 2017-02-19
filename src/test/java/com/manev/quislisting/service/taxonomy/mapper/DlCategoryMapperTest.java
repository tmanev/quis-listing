package com.manev.quislisting.service.taxonomy.mapper;

import com.manev.quislisting.domain.taxonomy.builder.TermBuilder;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.domain.taxonomy.discriminator.builder.DlCategoryBuilder;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class DlCategoryMapperTest {

    private DlCategoryMapper dlCategoryMapper = new DlCategoryMapper();

    @Test
    public void dlCategoryToDlCategoryDtoFlat() throws Exception {
        List<DlCategory> dlCategoryList = new ArrayList<>();
        DlCategory dlCat1 = DlCategoryBuilder.aDlCategory().withId(1L).withTerm(TermBuilder.aTerm().withId(1L).build()).build();
        dlCategoryList.add(dlCat1);
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(2L).withTerm(TermBuilder.aTerm().withId(1L).build()).build());
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(3L).withParent(dlCat1).withTerm(TermBuilder.aTerm().withId(1L).build()).build());
        DlCategory dlCat4 = DlCategoryBuilder.aDlCategory().withId(4L).withParent(dlCat1).withTerm(TermBuilder.aTerm().withId(1L).build()).build();
        dlCategoryList.add(dlCat4);
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(5L).withParent(dlCat1).withTerm(TermBuilder.aTerm().withId(1L).build()).build());
        DlCategory dlCat6 = DlCategoryBuilder.aDlCategory().withId(6L).withParent(dlCat1).withTerm(TermBuilder.aTerm().withId(1L).build()).build();
        dlCategoryList.add(dlCat6);
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(7L).withParent(dlCat4).withTerm(TermBuilder.aTerm().withId(1L).build()).build());
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(8L).withParent(dlCat4).withTerm(TermBuilder.aTerm().withId(1L).build()).build());
        DlCategory dlCat9 = DlCategoryBuilder.aDlCategory().withId(9L).withParent(dlCat6).withTerm(TermBuilder.aTerm().withId(1L).build()).build();
        dlCategoryList.add(dlCat9);
        DlCategory dlCat10 = DlCategoryBuilder.aDlCategory().withId(10L).withParent(dlCat9).withTerm(TermBuilder.aTerm().withId(1L).build()).build();
        dlCategoryList.add(dlCat10);
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(11L).withParent(dlCat9).withTerm(TermBuilder.aTerm().withId(1L).build()).build());
        dlCategoryList.add(DlCategoryBuilder.aDlCategory().withId(12L).withParent(dlCat10).withTerm(TermBuilder.aTerm().withId(1L).build()).build());

        PageImpl<DlCategory> page = new PageImpl<>(dlCategoryList);

        List<DlCategoryDTO> dlCategoryDTOS = dlCategoryMapper.dlCategoryToDlCategoryDtoFlat(page);
        assertEquals(dlCategoryList.size(), dlCategoryDTOS.size());
        assertEquals(0, findById(1L, dlCategoryDTOS).getDepthLevel());
        assertEquals(0, findById(2L, dlCategoryDTOS).getDepthLevel());
        assertEquals(1, findById(3L, dlCategoryDTOS).getDepthLevel());
        assertEquals(1, findById(4L, dlCategoryDTOS).getDepthLevel());
        assertEquals(1, findById(5L, dlCategoryDTOS).getDepthLevel());
        assertEquals(1, findById(6L, dlCategoryDTOS).getDepthLevel());
        assertEquals(2, findById(7L, dlCategoryDTOS).getDepthLevel());
        assertEquals(2, findById(8L, dlCategoryDTOS).getDepthLevel());
        assertEquals(2, findById(9L, dlCategoryDTOS).getDepthLevel());
        assertEquals(3, findById(10L, dlCategoryDTOS).getDepthLevel());
        assertEquals(3, findById(11L, dlCategoryDTOS).getDepthLevel());
        assertEquals(4, findById(12L, dlCategoryDTOS).getDepthLevel());
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