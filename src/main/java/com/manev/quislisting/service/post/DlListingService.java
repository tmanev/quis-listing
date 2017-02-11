package com.manev.quislisting.service.post;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.post.PostRepository;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import com.manev.quislisting.service.taxonomy.NavMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
