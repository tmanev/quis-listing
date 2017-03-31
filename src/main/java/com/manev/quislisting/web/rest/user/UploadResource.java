package com.manev.quislisting.web.rest.user;

import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UploadService;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.dto.FileMeta;
import com.manev.quislisting.service.dto.FileUploadResponse;
import com.manev.quislisting.service.post.AttachmentService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import javax.ws.rs.NotAuthorizedException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.manev.quislisting.service.storage.StorageService.DL_THUMBNAIL;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_USER_UPLOAD;

@RestController
@RequestMapping(RESOURCE_API_USER_UPLOAD)
public class UploadResource {

    private static final String DELETE = "DELETE";
    private static final String ENTITY_NAME = "Attachment";
    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private final UploadService uploadService;
    private final AttachmentService attachmentService;

    public UploadResource(UploadService uploadService, AttachmentService attachmentService) {
        this.uploadService = uploadService;
        this.attachmentService = attachmentService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> handleFileUpload(@RequestParam("files[]") MultipartFile[] files) throws IOException, RepositoryException, URISyntaxException {
        List<FileMeta> fileMetaList = new ArrayList<>();

        for (MultipartFile file : files) {
            AttachmentDTO attachmentDTO = uploadService.uploadFile(file);
            AttachmentMetadata.ImageResizeMeta imageThumbnailResizeMeta = attachmentDTO.getAttachmentMetadata().getImageResizeMetaByName(DL_THUMBNAIL);

            FileMeta fileMeta = new FileMeta(attachmentDTO.getName(), attachmentDTO.getAttachmentMetadata().getSize(),
                    "/content/files" + attachmentDTO.getAttachmentMetadata().getFile(),
                    "/content/files" + imageThumbnailResizeMeta.getDetail().getFile(),
                    RESOURCE_API_USER_UPLOAD + "/" + attachmentDTO.getId(), DELETE);

            fileMetaList.add(fileMeta);
        }

        return ResponseEntity.ok(new FileUploadResponse(fileMetaList));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUpload(@PathVariable Long id) {
        log.debug("REST request to delete upload : {}", id);
        AttachmentDTO attachmentDTO = attachmentService.findOne(id);
        if (!isUserLegitForDelete(attachmentDTO)) {
            throw new NotAuthorizedException("Deleting not allowed", attachmentDTO);
        }
        uploadService.delete(attachmentDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString()))
                .build();
    }

    private boolean isUserLegitForDelete(AttachmentDTO one) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin();
        String login = one.getAuthor().getLogin();
        return login.equals(currentUserLogin);
    }
}
