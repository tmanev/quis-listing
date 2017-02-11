package com.manev.quislisting.service.post.dto;

import java.util.List;
import java.util.Map;

public class DlListingDTO extends AbstractPostDTO {

    private String title;
    private String name;
    private String description;
    private List<Long> categoryIds;
    private Map<String, String> contentFields;
    private List<String> attachments;
    private Long locationId;
    private String addressLine1;

}
