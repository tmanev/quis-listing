package com.manev.quislisting.web;

import com.manev.quislisting.domain.AttachmentStreamResource;
import com.manev.quislisting.service.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/content")
public class ContentController {

    private final StorageService storageService;

    public ContentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/{year}/{month}/{day}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable(name = "year") String yearStr,
                                              @PathVariable(name = "month") String monthStr,
                                              @PathVariable(name = "day") String dayStr,
                                              @PathVariable String filename) {
        Integer year = Integer.valueOf(yearStr);
        Integer month = Integer.valueOf(monthStr);
        Integer day = Integer.valueOf(dayStr);

        AttachmentStreamResource attachmentStreamResource = storageService.loadAsResource("/"
                + year + "/"
                + String.format("%02d", month) + "/"
                + String.format("%02d", day) + "/"
                + filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, attachmentStreamResource.getMimeType())
                .body(attachmentStreamResource.getResource());
    }

}
