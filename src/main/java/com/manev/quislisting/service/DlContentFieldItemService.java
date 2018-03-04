package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlContentField;
import com.manev.quislisting.domain.DlContentFieldItem;
import com.manev.quislisting.domain.DlContentFieldItemGroup;
import com.manev.quislisting.domain.qlml.QlString;
import com.manev.quislisting.repository.DlContentFieldItemGroupRepository;
import com.manev.quislisting.repository.DlContentFieldItemRepository;
import com.manev.quislisting.repository.DlContentFieldRepository;
import com.manev.quislisting.service.dto.DlContentFieldItemDTO;
import com.manev.quislisting.service.mapper.DlContentFieldItemMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DlContentFieldItemService {

    private DlContentFieldItemMapper dlContentFieldItemMapper;
    private DlContentFieldItemRepository dlContentFieldItemRepository;
    private DlContentFieldRepository dlContentFieldRepository;
    private DlContentFieldItemGroupRepository dlContentFieldItemGroupRepository;

    public DlContentFieldItemService(DlContentFieldItemMapper dlContentFieldItemMapper, DlContentFieldItemRepository dlContentFieldItemRepository, DlContentFieldRepository dlContentFieldRepository, DlContentFieldItemGroupRepository dlContentFieldItemGroupRepository) {
        this.dlContentFieldItemMapper = dlContentFieldItemMapper;
        this.dlContentFieldItemRepository = dlContentFieldItemRepository;
        this.dlContentFieldRepository = dlContentFieldRepository;
        this.dlContentFieldItemGroupRepository = dlContentFieldItemGroupRepository;
    }

    public DlContentFieldItemDTO save(DlContentFieldItemDTO dlContentFieldItemDTO, Long dlContentFieldId) {
        DlContentFieldItem dlContentFieldItem;
        if (dlContentFieldItemDTO.getId() != null) {
            DlContentFieldItem one = dlContentFieldItemRepository.findOne(dlContentFieldItemDTO.getId());
            dlContentFieldItem = dlContentFieldItemMapper.dlContentFieldItemDTOToDlContentFieldItem(one, dlContentFieldItemDTO);
        } else {
            dlContentFieldItem = dlContentFieldItemMapper.dlContentFieldItemDTOToDlContentFieldItem(dlContentFieldItemDTO);
        }

        setDlContentField(dlContentFieldId, dlContentFieldItem);
        setParent(dlContentFieldItemDTO, dlContentFieldItem);
        setDlContentFieldItemGroup(dlContentFieldItemDTO, dlContentFieldItem);
        dlContentFieldItemRepository.save(dlContentFieldItem);

        saveQlString(dlContentFieldItem);

        return dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, null);
    }

    private void setDlContentFieldItemGroup(DlContentFieldItemDTO dlContentFieldItemDTO, DlContentFieldItem dlContentFieldItem) {
        Long dlContentFieldItemGroupId = dlContentFieldItemDTO.getDlContentFieldItemGroupId();
        if (dlContentFieldItemGroupId != null) {
            DlContentFieldItemGroup group = getDlContentFieldItemGroup(dlContentFieldItemGroupId);
            dlContentFieldItem.setDlContentFieldItemGroup(group);
        }
    }

    public Page<DlContentFieldItemDTO> findAll(Pageable pageable, Long dlContentFieldId, Long parentId, Long dlContentFieldItemGroupId) {
        DlContentField one = dlContentFieldRepository.findOne(dlContentFieldId);
        DlContentFieldItemGroup dlContentFieldItemGroup = getDlContentFieldItemGroup(dlContentFieldItemGroupId);
        Page<DlContentFieldItem> dlContentFieldItems;
        DlContentFieldItem parent = getDlContentFieldItem(parentId);

        if (dlContentFieldItemGroup != null) {
            dlContentFieldItems = dlContentFieldItemRepository.findAllByDlContentFieldAndParentAndDlContentFieldItemGroupOrderByOrderNum(pageable, one, parent, dlContentFieldItemGroup);
        } else {
            dlContentFieldItems = dlContentFieldItemRepository.findAllByDlContentFieldAndParentOrderByOrderNum(pageable, one, parent);
        }

        return dlContentFieldItems.map(dlContentFieldItem -> dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(dlContentFieldItem, null));
    }

    private DlContentFieldItem getDlContentFieldItem(Long parentId) {
        if (parentId!=null) {
            return dlContentFieldItemRepository.findOne(parentId);
        }
        return null;
    }

    private DlContentFieldItemGroup getDlContentFieldItemGroup(Long dlContentFieldItemGroupId) {
        if (dlContentFieldItemGroupId != null) {
            return dlContentFieldItemGroupRepository.findOne(dlContentFieldItemGroupId);
        }
        return null;
    }

    public DlContentFieldItemDTO findOne(Long id, String languageCode) {
        DlContentFieldItem result = dlContentFieldItemRepository.findOne(id);
        return result != null ? dlContentFieldItemMapper.dlContentFieldItemToDlContentFieldItemDTO(result, languageCode) : null;
    }

    public void delete(Long id) {
        dlContentFieldItemRepository.delete(id);
    }

    private void setDlContentField(Long dlContentFieldId, DlContentFieldItem dlContentFieldItem) {
        DlContentField one = dlContentFieldRepository.findOne(dlContentFieldId);
        one.addDlContentFieldItem(dlContentFieldItem);
        dlContentFieldItem.setDlContentField(one);
    }

    private void setParent(DlContentFieldItemDTO dlContentFieldItemDTO, DlContentFieldItem dlContentFieldItem) {
        DlContentFieldItemDTO parent = dlContentFieldItemDTO.getParent();
        if (parent != null) {
            DlContentFieldItem one = dlContentFieldItemRepository.findOne(parent.getId());
            dlContentFieldItem.setParent(one);
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
