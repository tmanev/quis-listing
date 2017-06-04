package com.manev.quislisting.service.taxonomy;

import com.manev.quislisting.domain.TranslationGroup;
import com.manev.quislisting.domain.taxonomy.discriminator.PostCategory;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.taxonomy.PostCategoryRepository;
import com.manev.quislisting.service.taxonomy.dto.PostCategoryDTO;
import com.manev.quislisting.service.taxonomy.mapper.PostCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PostCategoryService {

    private final Logger log = LoggerFactory.getLogger(PostCategoryService.class);

    private PostCategoryRepository postCategoryRepository;

    private TranslationGroupRepository translationGroupRepository;

    private PostCategoryMapper postCategoryMapper;

    public PostCategoryService(PostCategoryRepository postCategoryRepository, PostCategoryMapper postCategoryMapper,
                               TranslationGroupRepository translationGroupRepository) {
        this.postCategoryRepository = postCategoryRepository;
        this.postCategoryMapper = postCategoryMapper;
        this.translationGroupRepository = translationGroupRepository;
    }

    public PostCategoryDTO save(PostCategoryDTO postCategoryDTO) {
        log.debug("Request to save PostCategory : {}", postCategoryDTO);

        PostCategory postCategory = postCategoryMapper.postCategoryDTOToPostCategory(postCategoryDTO);
        if (postCategoryDTO.getTranslationGroupId() != null) {
            postCategory.getTranslation().setTranslationGroup(translationGroupRepository.findOne(postCategoryDTO.getTranslationGroupId()));
        } else {
            postCategory.getTranslation().setTranslationGroup(new TranslationGroup());
        }
        if (postCategoryDTO.getParentId() != null) {
            postCategory.setParent(postCategoryRepository.findOne(postCategoryDTO.getParentId()));
        }
        postCategory = postCategoryRepository.save(postCategory);
        return postCategoryMapper.postCategoryToPostCategoryDTO(postCategory);
    }

    public Page<PostCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostCategory");
        Page<PostCategory> result = postCategoryRepository.findAll(pageable);
        return result.map(postCategoryMapper::postCategoryToPostCategoryDTO);
    }

    @Transactional(readOnly = true)
    public PostCategoryDTO findOne(Long id) {
        log.debug("Request to get PostCategory : {}", id);
        PostCategory result = postCategoryRepository.findOne(id);
        return result != null ? postCategoryMapper.postCategoryToPostCategoryDTO(result) : null;
    }

    public void delete(Long id) {
        log.debug("Request to delete PostCategory : {}", id);
        postCategoryRepository.delete(id);
    }

}
