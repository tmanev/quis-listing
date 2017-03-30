package com.manev.quislisting.web.rest.user;

import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.UploadService;
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

import static com.manev.quislisting.web.rest.Constants.RESOURCE_API_USER_UPLOAD;

@RestController
@RequestMapping(RESOURCE_API_USER_UPLOAD)
public class UploadResource {

    private static final String ENTITY_NAME = "Attachment";

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private final UploadService uploadService;
    private final AttachmentService attachmentService;

    public UploadResource(UploadService uploadService, AttachmentService attachmentService) {
        this.uploadService = uploadService;
        this.attachmentService = attachmentService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AttachmentDTO>> handleFileUpload(@RequestParam("files[]") MultipartFile[] files) throws IOException, RepositoryException, URISyntaxException {
        List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
        for (MultipartFile file : files) {
            attachmentDTOList.add(uploadService.uploadFile(file));
        }

        return ResponseEntity.ok(attachmentDTOList);
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
