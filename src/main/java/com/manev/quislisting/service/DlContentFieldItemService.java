package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.DlContentFieldItemRepository;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.service.mapper.DlContentFieldItemMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DlContentFieldItemService {

    private DlContentFieldItemMapper dlContentFieldItemMapper;
    private DlContentFieldItemRepository dlContentFieldItemRepository;

    public DlContentFieldItemService(DlContentFieldItemMapper dlContentFieldItemMapper, DlContentFieldItemRepository dlContentFieldItemRepository) {
        this.dlContentFieldItemMapper = dlContentFieldItemMapper;
        this.dlContentFieldItemRepository = dlContentFieldItemRepository;
    }

    public List<DlContentFieldItem> saveDlContentFieldItems(DlContentField dlContentField, List<DlContentFieldItemDTO> dlContentFieldItemDTOs) {
        List<DlContentFieldItem> mappedItems = new ArrayList<>();
        for (DlContentFieldItemDTO dlContentFieldItemsDTO : dlContentFieldItemDTOs) {
            DlContentFieldItem dlContentFieldItem = dlContentFieldItemMapper.dlContentFieldItemDTOToDlContentFieldItem(dlContentFieldItemsDTO);
            dlContentFieldItem.setDlContentField(dlContentField);
            mappedItems.add(dlContentFieldItem);
        }

        List<DlContentFieldItem> existingItems = dlContentFieldItemRepository.findAllByDlContentField(dlContentField);
        List<DlContentFieldItem> existingItemsForMerge = new ArrayList<>();
        List<DlContentFieldItem> existingItemsForDelete = new ArrayList<>();
        Iterator<DlContentFieldItem> iterator = existingItems.iterator();
        while (iterator.hasNext()) {
            DlContentFieldItem existingItem = iterator.next();
            DlContentFieldItem newItem = findAndRemoveDlContentFieldItem(existingItem.getId(), mappedItems);
            if (newItem != null) {
                // merge values
                if (!newItem.getValue().equals(existingItem.getValue())) {
                    existingItem.setValue(newItem.getValue());
                    QlString qlString = existingItem.getQlString();
                    qlString.setStatus(0);
                }
                existingItemsForMerge.add(existingItem);
            } else {
                // prep item for deletion
                existingItemsForDelete.add(existingItem);
            }
        }

        // merge existing items
        List<DlContentFieldItem> existingSavedItems = dlContentFieldItemRepository.save(existingItemsForMerge);

        // delete items selected for deletion
        dlContentFieldItemRepository.delete(existingItemsForDelete);

        // add newly added items
        List<DlContentFieldItem> newlySavedItems = dlContentFieldItemRepository.save(mappedItems);
        // add qlStrings for the newly added items
        saveQlStringForDlContentFieldItems(newlySavedItems);

        existingSavedItems.addAll(newlySavedItems);

        return existingSavedItems;
    }

    private DlContentFieldItem findAndRemoveDlContentFieldItem(Long id, List<DlContentFieldItem> dlContentFieldItems) {
        DlContentFieldItem result = null;
        Iterator<DlContentFieldItem> iterator = dlContentFieldItems.iterator();
        while (iterator.hasNext()) {
            DlContentFieldItem next = iterator.next();
            if (next.getId().equals(id)) {
                result = next;
                iterator.remove();
                break;
            }
        }

        return result;
    }

    private void saveQlStringForDlContentFieldItems(List<DlContentFieldItem> dlContentFieldItems) {
        if (dlContentFieldItems != null && !dlContentFieldItems.isEmpty()) {
            for (DlContentFieldItem dlContentFieldItem : dlContentFieldItems) {
                saveQlString(dlContentFieldItem);
            }
        }
    }

    private void saveQlString(DlContentFieldItem dlContentFieldItem) {
        if (dlContentFieldItem.getQlString() == null) {
            dlContentFieldItem.setQlString(new QlString().languageCode("en").context("dl-content-field-item").name("dl-content-field-item-#" + dlContentFieldItem.getId()).value(dlContentFieldItem.getValue()).status(0));
        } else {
            QlString qlString = dlContentFieldItem.getQlString();
            if (!qlString.getValue().equals(dlContentFieldItem.getValue())) {
                qlString.setValue(dlContentFieldItem.getValue());
                qlString.setStatus(0);
            }
        }

        dlContentFieldItemRepository.save(dlContentFieldItem);
    }

}
