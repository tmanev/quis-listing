package com.manev.quislisting.service.post.mapper;

import com.manev.quislisting.domain.post.PostMeta;
import com.manev.quislisting.domain.post.discriminator.QlPage;
import com.manev.quislisting.service.post.dto.QlPageDTO;
import com.manev.quislisting.service.taxonomy.mapper.DlCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class QlPageMapper {

    private final Logger log = LoggerFactory.getLogger(QlPageMapper.class);



    public QlPage pageDTOtoPage(QlPageDTO qlPageDTO) {
        QlPage qlPage = new QlPage();
        qlPage.setId(qlPageDTO.getId());
        qlPage.setContent(qlPageDTO.getContent());
        qlPage.setCreated(qlPageDTO.getCreated());
        qlPage.setModified(qlPageDTO.getModified());
        qlPage.setName(qlPageDTO.getName());
        qlPage.setStatus(qlPageDTO.getStatus());
        qlPage.setTitle(qlPageDTO.getTitle());

        return qlPage;
    }

    public QlPageDTO pageToPageDTO(QlPage qlPage) {
        QlPageDTO qlPageDTO = new QlPageDTO();
        qlPageDTO.setId(qlPage.getId());
        qlPageDTO.setTitle(qlPage.getTitle());
        qlPageDTO.setName(qlPage.getName());
        qlPageDTO.setContent(qlPage.getContent());
        qlPageDTO.setCreated(qlPage.getCreated());
        qlPageDTO.setModified(qlPage.getModified());
        qlPageDTO.setStatus(qlPage.getStatus());
        qlPageDTO.setViews(qlPage.getPostMetaValue(PostMeta.META_KEY_POST_VIEWS_COUNT));



        return qlPageDTO;
    }




}
