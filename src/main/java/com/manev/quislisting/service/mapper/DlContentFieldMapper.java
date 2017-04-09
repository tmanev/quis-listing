package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DlContentFieldMapper {

    private DlCategoryMapper dlCategoryMapper;
    private DlContentFieldItemMapper dlContentFieldItemMapper;

    public DlContentFieldMapper(DlCategoryMapper dlCategoryMapper, DlContentFieldItemMapper dlContentFieldItemMapper) {
        this.dlCategoryMapper = dlCategoryMapper;
        this.dlContentFieldItemMapper = dlContentFieldItemMapper;
    }

    public DlContentField dlContentFieldDTOToDlContentField(DlContentField dlContentField, DlContentFieldDTO dlContentFieldDTO) {
        return dlContentField
                .id(dlContentFieldDTO.getId())
                .coreField(dlContentFieldDTO.getCoreField())
                .orderNum(dlContentFieldDTO.getOrderNum())
                .name(dlContentFieldDTO.getName())
                .slug(dlContentFieldDTO.getSlug())
                .description(dlContentFieldDTO.getDescription())
                .type(dlContentFieldDTO.getType())
                .iconImage(dlContentFieldDTO.getIconImage())
                .required(dlContentFieldDTO.getRequired())
                .hasConfiguration(dlContentFieldDTO.getHasConfiguration())
                .hasSearchConfiguration(dlContentFieldDTO.getHasSearchConfiguration())
                .canBeOrdered(dlContentFieldDTO.getCanBeOrdered())
                .hideName(dlContentFieldDTO.getHideName())
                .onExcerptPage(dlContentFieldDTO.getOnExcerptPage())
                .onListingPage(dlContentFieldDTO.getOnListingPage())
                .onSearchForm(dlContentFieldDTO.getOnSearchForm())
                .onMap(dlContentFieldDTO.getOnMap())
                .onAdvancedSearchForm(dlContentFieldDTO.getOnAdvancedSearchForm())
                .dlCategories(getDlCategories(dlContentFieldDTO.getDlCategories()))
                .options(dlContentFieldDTO.getOptions())
                .searchOptions(dlContentFieldDTO.getSearchOptions())
                ;
    }


    public DlContentFieldDTO dlContentFieldToDlContentFieldDTO(DlContentField dlContentField) {
        return new DlContentFieldDTO()
                .id(dlContentField.getId())
                .coreField(dlContentField.getCoreField())
                .orderNum(dlContentField.getOrderNum())
                .name(dlContentField.getName())
                .slug(dlContentField.getSlug())
                .description(dlContentField.getDescription())
                .type(dlContentField.getType())
                .iconImage(dlContentField.getIconImage())
                .required(dlContentField.getRequired())
                .hasConfiguration(dlContentField.getHasConfiguration())
                .hasSearchConfiguration(dlContentField.getHasSearchConfiguration())
                .canBeOrdered(dlContentField.getCanBeOrdered())
                .hideName(dlContentField.getHideName())
                .onExcerptPage(dlContentField.getOnExcerptPage())
                .onListingPage(dlContentField.getOnListingPage())
                .onSearchForm(dlContentField.getOnSearchForm())
                .onMap(dlContentField.getOnMap())
                .onAdvancedSearchForm(dlContentField.getOnAdvancedSearchForm())
                .options(dlContentField.getOptions())
                .searchOptions(dlContentField.getSearchOptions())
                .dlCategories(getDlCategoriesDTO(dlContentField.getDlCategories()))
                .dlContentFieldItems(getDlContentFieldsDTO(dlContentField.getDlContentFieldItems()));
    }

    public DlContentField dlContentFieldDTOToDlContentField(DlContentFieldDTO dlContentFieldDTO) {
        return dlContentFieldDTOToDlContentField(new DlContentField(), dlContentFieldDTO);
    }

    private Set<DlCategory> getDlCategories(List<DlCategoryDTO> dlCategoryDTOList) {
        if (dlCategoryDTOList != null && !dlCategoryDTOList.isEmpty()) {
            Set<DlCategory> dlCategories = new HashSet<>();
            for (DlCategoryDTO dlCategoryDTO : dlCategoryDTOList) {
                dlCategories.add(dlCategoryMapper.dlCategoryDTOTodlCategory(dlCategoryDTO));
            }
            return dlCategories;
        }
        return null;
    }

    private List<DlCategoryDTO> getDlCategoriesDTO(Set<DlCategory> dlCategories) {
        if (dlCategories != null && !dlCategories.isEmpty()) {
            List<DlCategoryDTO> dlCategoryDTOList = new ArrayList<>();
            for (DlCategory dlCategory : dlCategories) {
                dlCategoryDTOList.add(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory));
            }
            return dlCategoryDTOList;
        }
        return null;
    }

//    private Set<DlContentFieldItem> getDlContentFieldItems(List<DlContentFieldItemDTO> dlContentFieldItemDTOS) {
//        if (dlContentFieldItemDTOS != null && !dlContentFieldItemDTOS.isEmpty()) {
//            Set<DlContentFieldItem> result = new HashSet<>();
//            for (DlContentFieldItemDTO dlContentFieldItem : dlContentFieldItemDTOS) {
//                result.add(dlContentFieldItemMapper.dlContentFieldItemDTOToDlContentFieldItem(dlContentFieldItem));
//            }
//            return result;
//        }
//        return null;
//    }

    private List<DlContentFieldItemDTO> getDlContentFieldsDTO(Set<DlContentFieldItem> dlContentFieldItems) {
        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
            List<DlContentFieldItemDTO> result = new ArrayList<>();
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                result.add(dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem));
            }
            return result;
        }
        return null;
    }



}
