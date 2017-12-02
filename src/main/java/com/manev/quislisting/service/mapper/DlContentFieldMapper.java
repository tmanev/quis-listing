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
    private DlContentFieldGroupMapper dlContentFieldGroupMapper;

    public DlContentFieldMapper(DlCategoryMapper dlCategoryMapper, DlContentFieldItemMapper dlContentFieldItemMapper, DlContentFieldGroupMapper dlContentFieldGroupMapper) {
        this.dlCategoryMapper = dlCategoryMapper;
        this.dlContentFieldItemMapper = dlContentFieldItemMapper;
        this.dlContentFieldGroupMapper = dlContentFieldGroupMapper;
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
                .dlContentFieldGroup(dlContentFieldDTO.getDlContentFieldGroup() != null ?
                        dlContentFieldGroupMapper.dlContentFieldGroupDTOToDlContentFieldGroup(dlContentFieldDTO.getDlContentFieldGroup()) : null)
                ;
    }


    public DlContentFieldDTO dlContentFieldToDlContentFieldDTO(DlContentField dlContentField, String languageCode) {
        String translatedName = getTranslatedName(dlContentField, languageCode);
        return new DlContentFieldDTO()
                .id(dlContentField.getId())
                .coreField(dlContentField.getCoreField())
                .orderNum(dlContentField.getOrderNum())
                .name(dlContentField.getName())
                .translatedName(translatedName)
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
                .dlContentFieldItems(getDlContentFieldsDTO(dlContentField.getDlContentFieldItems(), languageCode))
                .dlContentFieldGroup(dlContentField.getDlContentFieldGroup() != null ?
                        dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentField.getDlContentFieldGroup()) : null)
                ;
    }

    private String getTranslatedName(DlContentField dlContentField, String languageCode) {
        String translation = TranslateUtil.getTranslatedString(dlContentField, languageCode);
        if (translation != null) return translation;
        return dlContentField.getName();
    }

    public DlContentField dlContentFieldDTOToDlContentField(DlContentFieldDTO dlContentFieldDTO) {
        return dlContentFieldDTOToDlContentField(new DlContentField(), dlContentFieldDTO);
    }

    private Set<DlCategory> getDlCategories(List<DlCategoryDTO> dlCategoryDTOList) {
        Set<DlCategory> dlCategories = new HashSet<>();
        if (dlCategoryDTOList != null && !dlCategoryDTOList.isEmpty()) {
            for (DlCategoryDTO dlCategoryDTO : dlCategoryDTOList) {
                dlCategories.add(dlCategoryMapper.dlCategoryDtoToDlCategory(dlCategoryDTO));
            }
        }
        return dlCategories;
    }

    private List<DlCategoryDTO> getDlCategoriesDTO(Set<DlCategory> dlCategories) {
        List<DlCategoryDTO> dlCategoryDTOList = new ArrayList<>();
        if (dlCategories != null && !dlCategories.isEmpty()) {
            for (DlCategory dlCategory : dlCategories) {
                dlCategoryDTOList.add(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory));
            }
        }
        return dlCategoryDTOList;
    }

    private List<DlContentFieldItemDTO> getDlContentFieldsDTO(Set<DlContentFieldItem> dlContentFieldItems, String languageCode) {
        List<DlContentFieldItemDTO> result = new ArrayList<>();
        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                result.add(dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, languageCode));
            }
        }

        return result;
    }


}
