package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.discriminator.Attachment;
import com.manev.quislisting.repository.UserRepository;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.security.SecurityUtils;
import com.manev.quislisting.service.dto.AttachmentMetadata;
import com.manev.quislisting.service.post.dto.AttachmentDTO;
import com.manev.quislisting.service.post.mapper.AttachmentMapper;
import com.manev.quislisting.service.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentService.class);
    private PostRepository<Attachment> postRepository;
    private AttachmentMapper attachmentMapper;
    private StorageService storageService;
    private UserRepository userRepository;

    public AttachmentService(PostRepository<Attachment> postRepository, AttachmentMapper attachmentMapper,
                             StorageService storageService, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.attachmentMapper = attachmentMapper;
        this.storageService = storageService;
        this.userRepository = userRepository;
    }

    public AttachmentDTO saveAttachmentByFileUpload(AttachmentDTO attachmentDTO) {
        log.debug("Request to save AttachmentDTO : {}", attachmentDTO);

        Attachment attachment = attachmentMapper.attachmentDTOToAttachment(attachmentDTO);
        attachment.setModified(ZonedDateTime.now());
        attachment = postRepository.save(attachment);
        return attachmentMapper.attachmentToAttachmentDTO(attachment);
    }

    public Page<AttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AttachmentDTOs");
        Page<Attachment> result = postRepository.findAll(pageable);
        return result.map(attachmentMapper::attachmentToAttachmentDTO);
    }

    @Transactional(readOnly = true)
    public AttachmentDTO findOne(Long id) {
        log.debug("Request to get AttachmentDTO: {}", id);
        Attachment result = postRepository.findOne(id);
        return result != null ? attachmentMapper.attachmentToAttachmentDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete AttachmentDTO : {}", id);
        Attachment attachment = postRepository.findOne(id);
        AttachmentDTO attachmentDTO = attachmentMapper.attachmentToAttachmentDTO(attachment);
        AttachmentMetadata attachmentMetadata = attachmentDTO.getAttachmentMetadata();
        List<AttachmentMetadata.ImageResizeMeta> imageResizeMetas = attachmentMetadata.getImageResizeMetas();


        List<String> filePaths = new ArrayList<>();
        filePaths.add(attachmentMetadata.getFile());
        for (AttachmentMetadata.ImageResizeMeta imageResizeMeta : imageResizeMetas) {
            filePaths.add(imageResizeMeta.getDetail().getFile());
        }

        try {
            storageService.delete(filePaths);
            postRepository.delete(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

    }

    public AttachmentDTO saveAttachmentByFileUpload(MultipartFile file) {
        try {
            AttachmentDTO attachmentDTO = storageService.store(file);
            Attachment attachment = attachmentMapper.attachmentDTOToAttachment(attachmentDTO);
            attachment.setStatus(Attachment.Status.TEMP.getValue());
            attachment.setCreated(attachmentDTO.getCreated());
            attachment.setModified(attachmentDTO.getModified());

            Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
            attachment.setUser(oneByLogin.get());

            attachment = postRepository.save(attachment);
            return attachmentMapper.attachmentToAttachmentDTO(attachment);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        return null;
    }

    public AttachmentDTO save(AttachmentDTO attachmentDTO) {
        return null;
    }
}
