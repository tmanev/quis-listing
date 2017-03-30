package com.manev.quislisting.web;

import com.google.common.collect.Lists;
import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.dto.FileMeta;
import com.manev.quislisting.service.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/content")
public class ContentController {

    private final StorageService storageService;

    public ContentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/{year}/{month}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable(name = "year") String yearStr,
                                              @PathVariable(name = "month") String monthStr,
                                              @PathVariable String filename) {
        Integer year = Integer.valueOf(yearStr);
        Integer month = Integer.valueOf(monthStr);

        AttachmentStreamResource attachmentStreamResource = storageService.loadAsResource("/"
                + String.valueOf(year) + "/"
                + String.format("%02d", month) + "/" + filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, attachmentStreamResource.getMimeType())
                .body(attachmentStreamResource.getResource());
    }

    @PostMapping("/upload")
    public Response handleFileUpload(@RequestParam("file") MultipartFile[] files) throws IOException, RepositoryException {
        List<FileMeta> metas = Lists.newArrayList();
        for (MultipartFile file : files) {
            storageService.store(file);

            FileMeta meta = new FileMeta("name1", 1000L,
                    "http://localhost/myrightescort-01/wp-content/uploads/2017/01/1787391936.jpg",
                    "http://localhost/myrightescort-01/wp-content/uploads/2017/01/1787391936.jpg",
                    "DELETE");
            metas.add(meta);
        }

        return Response.ok(metas, MediaType.APPLICATION_JSON_UTF8_VALUE).build();
    }
}
