package com.manev.quislisting.service;

import com.manev.quislisting.service.post.AttachmentService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.storage.StorageService;
import com.manev.quislisting.service.util.AttachmentUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class UploadService {

    private AttachmentService attachmentService;
    private StorageService storageService;

    public UploadService(AttachmentService attachmentService, StorageService storageService) {
        this.attachmentService = attachmentService;
        this.storageService = storageService;
    }

    public AttachmentDTO uploadFile(MultipartFile file) {
        AttachmentDTO savedAttachmentDTO = null;
        try {
            AttachmentDTO attachmentDTO = storageService.store(file);
            savedAttachmentDTO = attachmentService.saveAttachmentAsTemp(attachmentDTO);
        } catch (IOException | RepositoryException e) {
            e.printStackTrace();
        }
        return savedAttachmentDTO;
    }

    public AttachmentDTO uploadFileByAdmin(MultipartFile file) {
        AttachmentDTO savedAttachmentDTO = null;
        try {
            AttachmentDTO attachmentDTO = storageService.store(file);
            savedAttachmentDTO = attachmentService.saveAttachmentByAdmin(attachmentDTO);
        } catch (IOException | RepositoryException e) {
            e.printStackTrace();
        }
        return savedAttachmentDTO;
    }

    public void delete(Long id) {
        AttachmentDTO attachmentDTO = attachmentService.findOne(id);
        delete(attachmentDTO);
    }

    public void delete(AttachmentDTO attachmentDTO) {
        List<String> filePaths = AttachmentUtil.getFilePaths(attachmentDTO);
        try {
            storageService.delete(filePaths);
            attachmentService.delete(attachmentDTO.getId());
        } catch (IOException | RepositoryException e) {
            e.printStackTrace();
        }
    }

}
