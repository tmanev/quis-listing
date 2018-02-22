package com.manev.quislisting.service;

import com.manev.quislisting.domain.DlAttachment;
import com.manev.quislisting.domain.DlAttachmentResize;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.DlAttachmentRepository;
import com.manev.quislisting.repository.DlAttachmentResizeRepository;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.search.DlListingSearchRepository;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.dto.QlImageFile;
import com.manev.quislisting.service.post.mapper.AttachmentMapper;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.storage.StorageService;
import com.manev.quislisting.service.storage.components.QlFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

@Service
public class DlAttachmentRegenerateService {

    private final Logger log = LoggerFactory.getLogger(DlAttachmentRegenerateService.class);

    private DlAttachmentRepository dlAttachmentRepository;
    private DlAttachmentResizeRepository dlAttachmentResizeRepository;
    private StorageService storageService;
    private AttachmentMapper attachmentMapper;
    private DlListingRepository dlListingRepository;
    private DlListingMapper dlListingMapper;
    private DlListingSearchRepository dlListingSearchRepository;

    public DlAttachmentRegenerateService(DlAttachmentRepository dlAttachmentRepository, DlAttachmentResizeRepository dlAttachmentResizeRepository, StorageService storageService, AttachmentMapper attachmentMapper, DlListingRepository dlListingRepository, DlListingMapper dlListingMapper, DlListingSearchRepository dlListingSearchRepository) {
        this.dlAttachmentRepository = dlAttachmentRepository;
        this.dlAttachmentResizeRepository = dlAttachmentResizeRepository;
        this.storageService = storageService;
        this.attachmentMapper = attachmentMapper;
        this.dlListingRepository = dlListingRepository;
        this.dlListingMapper = dlListingMapper;
        this.dlListingSearchRepository = dlListingSearchRepository;
    }

    public void reGenerateImages() throws IOException {
        log.info("Running DlAttachmentRegenerateService -> reGenerateImages");
        long startTime = System.currentTimeMillis();

        int size = 10;
        long total = dlListingRepository.count();
        log.info("Total: {} listings to process. Page size is: ", total, size);
        for (int page = 0; page < (int) Math.ceil((double) total / size); page++) {
            Page<DlListing> all = dlListingRepository.findAll(new PageRequest(page, size));

            for (DlListing dlListing : all) {
                log.info("Processing listing with id: {}", dlListing.getId());
                Set<DlAttachment> dlAttachments = dlListing.getDlAttachments();
                log.info("Listing with id: {}, has: {} attachments.", dlListing.getId(), dlAttachments.size());
                for (DlAttachment dlAttachment : dlAttachments) {
                    File attachmentFile = storageService.getFile(dlAttachment.getPath());
                    moveOriginalImage(dlAttachment, attachmentFile);

                    QlImageFile qlImageFile = getQlImageFile(dlAttachment, attachmentFile);

                    Map<Long, String> attachmentResizeToRemove = getAttachmentResizesForRemoval(dlAttachment);

                    AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(dlAttachment);
                    storageService.storeResizes(attachmentDTO, qlImageFile);

                    DlAttachment dlAttachmentReplacement = attachmentMapper.attachmentDTOToAttachment(attachmentDTO);

                    Set<DlAttachmentResize> dlAttachmentResizes = dlAttachmentReplacement.getDlAttachmentResizes();
                    for (DlAttachmentResize dlAttachmentResize : dlAttachmentResizes) {
                        dlAttachmentResize.setDlAttachment(dlAttachment);
                        dlAttachment.addDlAttachmentResize(dlAttachmentResize);
                    }

                    dlAttachmentRepository.save(dlAttachment);

                    removeOldResizes(attachmentResizeToRemove);
                }

                DlListingDTO savedDlListingDTO = dlListingMapper.dlListingToDlListingDTO(dlListing);
                dlListingSearchRepository.save(savedDlListingDTO);
            }
        }

        log.info("DlAttachmentRegenerateService -> reGenerateImages - finished in {} milliseconds", System.currentTimeMillis() - startTime);
    }

    private void moveOriginalImage(DlAttachment dlAttachment, File attachmentFile) throws IOException {
        String path = dlAttachment.getPath();
        if (!path.contains("originals")) {
            log.info("Path: {}, does not contain 'originals' moving file.", path);

            Path parent = attachmentFile.toPath().getParent();
            File originals = new File(parent.toFile(), "originals");
            if (originals.exists() && originals.isDirectory()) {
                dlAttachment.setPath(moveToOriginals(dlAttachment, attachmentFile, originals));
            } else {
                Files.createDirectories(originals.toPath());
                dlAttachment.setPath(moveToOriginals(dlAttachment, attachmentFile, originals));
            }
        }
        dlAttachmentRepository.save(dlAttachment);
    }

    private Map<Long, String> getAttachmentResizesForRemoval(DlAttachment dlAttachment) {
        Map<Long, String> attachmentResizeToRemove = new LinkedHashMap<>();
        Set<DlAttachmentResize> dlAttachmentResizesForRemoval = dlAttachment.getDlAttachmentResizes();
        for (DlAttachmentResize dlAttachmentResize : dlAttachmentResizesForRemoval) {
            attachmentResizeToRemove.put(dlAttachmentResize.getId(), dlAttachmentResize.getPath());
        }
        return attachmentResizeToRemove;
    }

    private QlImageFile getQlImageFile(DlAttachment dlAttachment, File attachmentFile) throws IOException {
        QlImageFile qlImageFile = new QlImageFile();
        qlImageFile.setBufferedImage(ImageIO.read(attachmentFile));
        qlImageFile.setOriginalFilename(dlAttachment.getFileName());
        qlImageFile.setContentType(dlAttachment.getMimeType());
        return qlImageFile;
    }

    private String moveToOriginals(DlAttachment dlAttachment, File file, File originals) throws IOException {
        String generatedFileName = QlFileUtils.generateFileName(dlAttachment.getFileName());
        File newFile = new File(originals, generatedFileName);
        Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        List<String> newPaStringList = new ArrayList<>();
        // it is assumed that is stored under /year/month/day/originals/filename.ext
        Path path = newFile.toPath();
        newPaStringList.add(path.getFileName().toString());
        newPaStringList.add(File.separator);

        Path originalsPath = path.getParent();
        newPaStringList.add(originalsPath.getFileName().toString());
        newPaStringList.add(File.separator);

        Path day = originalsPath.getParent();
        newPaStringList.add(day.getFileName().toString());
        newPaStringList.add(File.separator);

        Path month = day.getParent();
        newPaStringList.add(month.getFileName().toString());
        newPaStringList.add(File.separator);

        Path year = month.getParent();
        newPaStringList.add(year.getFileName().toString());
        newPaStringList.add(File.separator);

        return getNewPath(newPaStringList);
    }

    private String getNewPath(List<String> pathParts) {
        ListIterator<String> stringListIterator = pathParts.listIterator(pathParts.size());
        StringBuilder sb = new StringBuilder();
        while (stringListIterator.hasPrevious()) {
            String previous = stringListIterator.previous();
            sb.append(previous);
        }
        return sb.toString();
    }

    private void removeOldResizes(Map<Long, String> attachmentResizeToRemove) {
        for (Map.Entry<Long, String> longStringEntry : attachmentResizeToRemove.entrySet()) {
            Long id = longStringEntry.getKey();
            String path = longStringEntry.getValue();
            log.info("Removing old resized attachments. id: {}, path: {}", id, path);
            dlAttachmentResizeRepository.delete(id);
            storageService.delete(path);
        }
    }

}
