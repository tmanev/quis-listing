package com.manev.quislisting.repository.search;

import com.manev.quislisting.service.post.dto.DlListingDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DlListing entity.
 */
public interface DlListingSearchRepository extends ElasticsearchRepository<DlListingDTO, Long> {
}
