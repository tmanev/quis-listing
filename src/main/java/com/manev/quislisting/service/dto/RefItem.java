package com.manev.quislisting.service.dto;

import com.manev.quislisting.service.post.dto.AbstractPostDTO;

public class RefItem extends AbstractPostDTO {

    public RefItem() {
        // default constructor
    }

    public RefItem(Long id, String name, String title) {
        setId(id);
        setName(name);
        setTitle(title);
    }

}
