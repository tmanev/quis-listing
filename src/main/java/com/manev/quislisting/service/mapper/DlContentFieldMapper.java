package com.manev.quislisting.service.mapper;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlContentFieldItemGroup;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.service.dto.DlContentFieldItemGroupDTO;
import com.manev.quislisting.service.taxonomy.dto.DlCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DlContentFieldMapper {

    private final Logger log = LoggerFactory.getLogger(DlContentFieldMapper.class);

    private DlCategoryMapper dlCategoryMapper;
    private DlContentFieldItemMapper dlContentFieldItemMapper;
    private DlContentFieldGroupMapper dlContentFieldGroupMapper;
    private DlContentFieldItemGroupMapper dlContentFieldItemGroupMapper;

    public DlContentFieldMapper(DlCategoryMapper dlCategoryMapper, DlContentFieldItemMapper dlContentFieldItemMapper, DlContentFieldGroupMapper dlContentFieldGroupMapper, DlContentFieldItemGroupMapper dlContentFieldItemGroupMapper) {
        this.dlCategoryMapper = dlCategoryMapper;
        this.dlContentFieldItemMapper = dlContentFieldItemMapper;
        this.dlContentFieldGroupMapper = dlContentFieldGroupMapper;
        this.dlContentFieldItemGroupMapper = dlContentFieldItemGroupMapper;
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
                .enabled(dlContentFieldDTO.getEnabled())
                ;
    }


    public DlContentFieldDTO dlContentFieldToDlContentFieldDTO(DlContentField dlContentField, String languageCode) {
        long start = System.currentTimeMillis();
        String translatedName = TranslateUtil.getTranslatedString(dlContentField, languageCode);
        String translatedDescription = TranslateUtil.getTranslatedStringDescription(dlContentField, languageCode);
        DlContentFieldDTO dlContentFieldDTO = new DlContentFieldDTO()
                .id(dlContentField.getId())
                .coreField(dlContentField.getCoreField())
                .orderNum(dlContentField.getOrderNum())
                .name(dlContentField.getName())
                .translatedName(translatedName)
                .slug(dlContentField.getSlug())
                .description(dlContentField.getDescription())
                .translatedDescription(translatedDescription)
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
                .dlContentFieldItems(getDlContentFieldItemsDTO(dlContentField, dlContentField.getDlContentFieldItems(), languageCode))
                .dlContentFieldItemGroups(getDlContentFieldItemGroupsDTO(dlContentField, languageCode))
                .dlContentFieldGroup(dlContentField.getDlContentFieldGroup() != null ?
                        dlContentFieldGroupMapper.dlContentFieldGroupToDlContentFieldGroupDTO(dlContentField.getDlContentFieldGroup()) : null)
                .enabled(dlContentField.getEnabled());
        log.info("dlContentFieldToDlContentFieldDTO for id: {}, name: {}, took: {} ms", dlContentField.getId(), dlContentField.getName(), System.currentTimeMillis() - start);
        return dlContentFieldDTO;
    }

    public DlContentField dlContentFieldDTOToDlContentField(DlContentFieldDTO dlContentFieldDTO) {
        return dlContentFieldDTOToDlContentField(new DlContentField(), dlContentFieldDTO);
    }

    private Set<DlCategory> getDlCategories(List<DlCategoryDTO> dlCategoryDTOList) {
        Set<DlCategory> dlCategories = new HashSet<>();
        if (!CollectionUtils.isEmpty(dlCategoryDTOList)) {
            for (DlCategoryDTO dlCategoryDTO : dlCategoryDTOList) {
                dlCategories.add(dlCategoryMapper.dlCategoryDtoToDlCategory(dlCategoryDTO));
            }
        }
        return dlCategories;
    }

    private List<DlCategoryDTO> getDlCategoriesDTO(Set<DlCategory> dlCategories) {
        List<DlCategoryDTO> dlCategoryDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dlCategories)) {
            for (DlCategory dlCategory : dlCategories) {
                dlCategoryDTOList.add(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory));
            }
        }
        return dlCategoryDTOList;
    }

    private List<DlContentFieldItemDTO> getDlContentFieldItemsDTO(DlContentField dlContentField, Set<DlContentFieldItem> dlContentFieldItems, String languageCode) {
        long start = System.currentTimeMillis();
        List<DlContentFieldItemDTO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dlContentFieldItems)) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                result.add(dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, languageCode));
            }
        }
        log.info("getDlContentFieldItemsDTO for id: {}, name: {}, took: {} ms", dlContentField.getId(), dlContentField.getName(), System.currentTimeMillis() - start);
        return result;
    }

    private List<DlContentFieldItemGroupDTO> getDlContentFieldItemGroupsDTO(DlContentField dlContentField, String languageCode) {
        long start = System.currentTimeMillis();


        Map<DlContentFieldItemGroup, List<DlContentFieldItemDTO>> groups = new LinkedHashMap<>();
        Set<DlContentFieldItem> dlContentFieldItems = dlContentField.getDlContentFieldItems();
        if (!CollectionUtils.isEmpty(dlContentFieldItems)) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                if (dlContentFieldItem.getDlContentFieldItemGroup() != null) {
                    List<DlContentFieldItemDTO> dlListingFieldItemDTOS = groups.get(dlContentFieldItem.getDlContentFieldItemGroup());
                    if (dlListingFieldItemDTOS == null) {
                        dlListingFieldItemDTOS = new ArrayList<>();
                    }
                    dlListingFieldItemDTOS.add(dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, languageCode));
                    groups.put(dlContentFieldItem.getDlContentFieldItemGroup(), dlListingFieldItemDTOS);
                }
            }
        }

        List<DlContentFieldItemGroupDTO> groupsModel = new ArrayList<>();
        for (Map.Entry<DlContentFieldItemGroup, List<DlContentFieldItemDTO>> entry : groups.entrySet()) {
            DlContentFieldItemGroupDTO dlListingFieldItemGroupModel = dlContentFieldItemGroupMapper.mapDto(entry.getKey(), languageCode);
            dlListingFieldItemGroupModel.setDlContentFieldItems(entry.getValue());
            groupsModel.add(dlListingFieldItemGroupModel);
        }

        log.info("getDlContentFieldItemsDTO for id: {}, name: {}, took: {} ms", dlContentField.getId(), dlContentField.getName(), System.currentTimeMillis() - start);
        return groupsModel;
    }


}
