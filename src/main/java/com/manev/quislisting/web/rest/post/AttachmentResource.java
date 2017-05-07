package com.manev.quislisting.web.rest.post;

import com.manev.quislisting.service.UploadService;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.dto.FileMeta;
import com.manev.quislisting.service.dto.FileUploadResponse;
import com.manev.quislisting.service.post.AttachmentService;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.web.rest.util.HeaderUtil;
import com.manev.quislisting.web.rest.util.PaginationUtil;
import com.manev.quislisting.web.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.manev.quislisting.service.storage.StorageService.DL_THUMBNAIL;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_ADMIN_ATTACHMENTS;
import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_USER_UPLOAD;

@RestController
@RequestMapping(RESOURCE_API_ADMIN_ATTACHMENTS)
public class AttachmentResource {

    public static final String ENTITY_NAME = "Attachment";
    private static final String DELETE = "DELETE";

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);
    private final AttachmentService attachmentService;
    private final UploadService uploadService;

    public AttachmentResource(AttachmentService attachmentService, UploadService uploadService) {
        this.attachmentService = attachmentService;
        this.uploadService = uploadService;
    }


    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> handleFileUpload(@RequestParam("files[]") MultipartFile[] files) throws IOException, RepositoryException, URISyntaxException {
        List<FileMeta> fileMetaList = new ArrayList<>();

        for (MultipartFile file : files) {
            AttachmentDTO attachmentDTO = uploadService.uploadFileByAdmin(file);
            AttachmentMetadata.ImageResizeMeta imageThumbnailResizeMeta = attachmentDTO.getAttachmentMetadata().getImageResizeMetaByName(DL_THUMBNAIL);

            FileMeta fileMeta = new FileMeta(attachmentDTO.getName(), attachmentDTO.getAttachmentMetadata().getSize(),
                    "/content/files" + attachmentDTO.getAttachmentMetadata().getFile(),
                    "/content/files" + imageThumbnailResizeMeta.getDetail().getFile(),
                    RESOURCE_API_ADMIN_ATTACHMENTS + "/" + attachmentDTO.getId(), DELETE);

            fileMetaList.add(fileMeta);
        }

        return ResponseEntity.ok(new FileUploadResponse(fileMetaList));
    }

    @PutMapping
    public ResponseEntity<AttachmentDTO> updateDlListing(@RequestBody AttachmentDTO attachmentDTO) throws URISyntaxException {
        log.debug("REST request to update AttachmentDTO : {}", attachmentDTO);
        if (attachmentDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "Update  cannot be without an ID")).body(null);
        }
        AttachmentDTO result = attachmentService.save(attachmentDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachmentDTO.getId().toString()))
                .body(result);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of AttachmentDTO");
        Page<AttachmentDTO> page = attachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, RESOURCE_API_ADMIN_ATTACHMENTS);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable Long id) {
        log.debug("REST request to get AttachmentDTO : {}", id);
        AttachmentDTO attachmentDTO = attachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(attachmentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.debug("REST request to delete AttachmentDTO : {}", id);
        uploadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
