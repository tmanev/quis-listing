package com.manev.quislisting.service.post.rebuildindex;

import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.repository.post.DlListingRepository;
import com.manev.quislisting.repository.search.DlListingSearchRepository;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.post.mapper.DlListingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DlListingRebuildService {

    private final Logger log = LoggerFactory.getLogger(DlListingRebuildService.class);

    private DlListingRepository dlListingRepository;
    private DlListingSearchRepository dlListingSearchRepository;
    private DlListingMapper dlListingMapper;

    public DlListingRebuildService(DlListingRepository dlListingRepository, DlListingSearchRepository dlListingSearchRepository, DlListingMapper dlListingMapper) {
        this.dlListingRepository = dlListingRepository;
        this.dlListingSearchRepository = dlListingSearchRepository;
        this.dlListingMapper = dlListingMapper;
    }

    public void rebuildElasticsearchData() {
        log.info("Running DlListingRebuildService -> rebuildElasticsearchData");
        long startTime = System.currentTimeMillis();

        Fetcher<DlListingRepository, DlListing> f = new Fetcher<DlListingRepository, DlListing>(dlListingRepository) {
            @Override
            public List<DlListing> fetch(Pageable pageRequest) {
                return dlListingRepository.findAll(pageRequest).getContent();
            }
        };

        PageableCollection<DlListing> pageableCollection = new PageableCollection<>(f);

        for (DlListing dlListing : pageableCollection) {
            DlListingDTO one = dlListingSearchRepository.findOne(dlListing.getId());
            if (one != null) {
                dlListingSearchRepository.delete(one);
            }

            DlListingDTO reBuildDlListingDTO = dlListingMapper.dlListingToDlListingDTO(dlListing);
            dlListingSearchRepository.save(reBuildDlListingDTO);
        }

        log.info("DlListingRebuildService -> rebuildElasticsearchData - finished in {} milliseconds", System.currentTimeMillis() - startTime);
    }

}
