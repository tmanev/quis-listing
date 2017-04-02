package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.User;
import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.DlListing;
import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.service.post.dto.Author;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DlListingMapper {

    private final Logger log = LoggerFactory.getLogger(DlListingMapper.class);


    private DlCategoryMapper dlCategoryMapper;

    public DlListingMapper(DlCategoryMapper dlCategoryMapper) {
        this.dlCategoryMapper = dlCategoryMapper;
    }

    public DlListing dlListingDTOToDlListing(DlListingDTO dlListingDTO) {
//        return DlListingBuilder.aDlListing()
//                .withId(dlListingDTO.getId())
//                .withTitle(dlListingDTO.getTitle()
//                .withStatus(dlListingDTO.getStatus())
//
//                ).build();
        return null;
    }

    public DlListingDTO dlListingToDlListingDTO(DlListing dlListing) {
        DlListingDTO dlListingDTO = new DlListingDTO();
        dlListingDTO.setId(dlListing.getId());
        dlListingDTO.setTitle(dlListing.getTitle());
        dlListingDTO.setName(dlListing.getName());
        dlListingDTO.setContent(dlListing.getContent());
        dlListingDTO.setCreated(dlListing.getCreated());
        dlListingDTO.setModified(dlListing.getModified());
        dlListingDTO.setStatus(dlListing.getPostMetaValue(PostMeta.META_KEY_LISTING_STATUS));
        dlListingDTO.setViews(dlListing.getPostMetaValue(PostMeta.META_KEY_POST_VIEWS_COUNT));

        setExpirationDate(dlListing, dlListingDTO);

        Set<DlCategory> dlCategories = dlListing.getDlCategories();
        if (dlCategories!=null && !dlCategories.isEmpty()) {
            for (DlCategory dlCategory : dlCategories) {
                dlListingDTO.addDlCategoryDto(dlCategoryMapper.dlCategoryToDlCategoryDTO(dlCategory));
            }
        }

        // TODO set images
        // TODO set content fields
        // TODO attached image as logo
        // TODO post views count
        // TODO location id
        // TODO address
        // TODO map coordinates
        // TODO map zoom
        // TODO clicks data
        // TODO total clicks

        setAuthor(dlListing, dlListingDTO);

        return dlListingDTO;
    }

    private void setAuthor(DlListing dlListing, DlListingDTO dlListingDTO) {
        User user = dlListing.getUser();

        dlListingDTO.setAuthor(new Author(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName()));
    }

    private void setExpirationDate(DlListing dlListing, DlListingDTO dlListingDTO) {
        String expirationDate = dlListing.getPostMetaValue(PostMeta.META_KEY_EXPIRATION_DATE);
        if (expirationDate != null) {
            try {
                dlListingDTO.setExpirationDate(expirationDate);
            } catch (NumberFormatException ex) {
                log.error("Expiration date : {} not a number", expirationDate, ex);
            } catch (IllegalArgumentException ex) {
                log.error("Expiration date : {} is a invalid argument", expirationDate, ex);
            }
        }
    }


}
