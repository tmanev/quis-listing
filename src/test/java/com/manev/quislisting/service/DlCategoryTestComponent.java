package com.manev.quislisting.service;

import com.manev.quislisting.domain.taxonomy.discriminator.DlCategory;
import com.manev.quislisting.repository.TranslationGroupRepository;
import com.manev.quislisting.repository.taxonomy.DlCategoryRepository;
import com.manev.quislisting.service.util.SlugUtil;
import com.manev.quislisting.web.rest.admin.DlCategoryAdminRestTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DlCategoryTestComponent {

    @Autowired
    private DlCategoryRepository dlCategoryRepository;

    @Autowired
    private TranslationGroupRepository translationGroupRepository;

    public DlCategory initCategory(String langKey) {
        DlCategory dlCategory = DlCategoryAdminRestTest.createEntity(langKey);
        return dlCategoryRepository.saveAndFlush(dlCategory);
    }

    public DlCategory initCategory(String name, String langKey) {
        DlCategory dlCategory = DlCategoryAdminRestTest.createEntity(langKey);
        dlCategory.setName(name);
        dlCategory.setSlug(SlugUtil.slugify(name));
        return dlCategoryRepository.saveAndFlush(dlCategory);
    }

    public DlCategory initCategory(DlCategory parent, String name, String langKey) {
        DlCategory dlCategory = DlCategoryAdminRestTest.createEntity(langKey);
        dlCategory.setName(name);
        dlCategory.setSlug(SlugUtil.slugify(name));
        dlCategory.setParent(parent);
        parent.addChildren(dlCategory);
        return dlCategoryRepository.saveAndFlush(dlCategory);
    }

    public DlCategory initCategory(Long translationGroupId, DlCategory parent, String name, String langKey) {
        DlCategory dlCategory = DlCategoryAdminRestTest.createEntity(langKey);
        dlCategory.setName(name);
        dlCategory.setSlug(SlugUtil.slugify(name));
        dlCategory.setParent(parent);
        parent.addChildren(dlCategory);

        dlCategory.getTranslation().setTranslationGroup(translationGroupRepository.findOne(translationGroupId));

        return dlCategoryRepository.saveAndFlush(dlCategory);
    }

    public DlCategory initCategory(Long translationGroupId, String name, String langKey) {
        DlCategory dlCategory = DlCategoryAdminRestTest.createEntity(langKey);
        dlCategory.setName(name);
        dlCategory.setSlug(SlugUtil.slugify(name));

        dlCategory.getTranslation().setTranslationGroup(translationGroupRepository.findOne(translationGroupId));

        return dlCategoryRepository.saveAndFlush(dlCategory);
    }

}
