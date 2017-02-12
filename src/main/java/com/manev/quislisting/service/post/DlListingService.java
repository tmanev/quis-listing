package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DlListingService {

    private final Logger log = LoggerFactory.getLogger(DlListingService.class);

    private PostRepository<DlListing> postRepository;

    private DlListingMapper dlListingMapper;

    public DlListingService(PostRepository<DlListing> postRepository, DlListingMapper dlListingMapper) {
        this.postRepository = postRepository;
        this.dlListingMapper = dlListingMapper;
    }

    public DlListingDTO save(DlListingDTO dlListingDTO) {
        log.debug("Request to save DlListingDTO : {}", dlListingDTO);

        DlListing dlListing = dlListingMapper.dlListingDTOToDlListing(dlListingDTO);
        dlListing = postRepository.save(dlListing);
        return dlListingMapper.dlListingToDlListingDTO(dlListing);
    }

    public Page<DlListingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DlListingDTO");
        Page<DlListing> result = postRepository.findAll(pageable);
        return result.map(dlListingMapper::dlListingToDlListingDTO);
    }

    @Transactional(readOnly = true)
    public DlListingDTO findOne(Long id) {
        log.debug("Request to get DlListingDTO: {}", id);
        DlListing result = postRepository.findOne(id);
        return result != null ? dlListingMapper.dlListingToDlListingDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete DlCategoryDTO : {}", id);
        postRepository.delete(id);
    }
}
