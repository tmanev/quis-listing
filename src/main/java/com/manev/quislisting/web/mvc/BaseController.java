package com.manev.quislisting.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manev.quislisting.domain.QlConfig;
import com.manev.quislisting.domain.QlMenuConfig;
import com.manev.quislisting.domain.QlMenuPosConfig;
import com.manev.quislisting.domain.qlml.Language;
import com.manev.quislisting.domain.qlml.LanguageTranslation;
import com.manev.quislisting.domain.taxonomy.discriminator.NavMenu;
import com.manev.quislisting.repository.qlml.LanguageRepository;
import com.manev.quislisting.repository.qlml.LanguageTranslationRepository;
import com.manev.quislisting.repository.taxonomy.NavMenuRepository;
import com.manev.quislisting.service.QlConfigService;
import com.manev.quislisting.service.post.StaticPageService;
import com.manev.quislisting.service.post.dto.DlListingDTO;
import com.manev.quislisting.service.taxonomy.DlLocationService;
import com.manev.quislisting.service.taxonomy.dto.DlLocationDTO;
import com.manev.quislisting.web.model.ActiveLanguageBean;
import com.manev.quislisting.web.model.JsTranslations;
import com.manev.quislisting.web.mvc.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BaseController {

    private static Logger log = LoggerFactory.getLogger(BaseController.class);

    protected static final String ATTRIBUTE_TITLE = "title";
    protected static final String PAGE_CLIENT_INDEX = "client/index";

    static final String REDIRECT = "redirect:/";

    @Autowired
    protected NavMenuRepository navMenuRepository;
    @Autowired
    protected QlConfigService qlConfigService;
    @Autowired
    protected LanguageRepository languageRepository;
    @Autowired
    protected LocaleResolver localeResolver;
    @Autowired
    protected StaticPageService staticPageService;
    @Autowired
    protected LanguageTranslationRepository languageTranslationRepository;
    @Autowired
    protected MessageSource messageSource;
    @Autowired
    private DlLocationService dlLocationService;

    @ModelAttribute("baseModel")
    public BaseModel baseModel(HttpServletRequest request) throws IOException {
        BaseModel baseModel = new BaseModel();

        Locale locale = localeResolver.resolveLocale(request);
        String language = locale.getLanguage();
        log.debug("Language from cookie: {}", language);

        QlConfig qlMenuConfigs = qlConfigService.findOneByKey("ql-menu-configurations");

        QlMenuConfig qlMenuPosConfig = new ObjectMapper().readValue(qlMenuConfigs.getValue(),
                QlMenuConfig.class);

        QlMenuPosConfig qlMenuPosByLanguageCode = findQlMenuPosByLanguageCode(language, qlMenuPosConfig.getQlMenuPosConfigs());
        if (qlMenuPosByLanguageCode != null) {
            Long topHeaderMenuRefId = qlMenuPosByLanguageCode.getTopHeaderMenuRefId();
            if (topHeaderMenuRefId != null) {
                NavMenu topHeaderMenu = navMenuRepository.findOne(topHeaderMenuRefId);
                baseModel.setTopHeaderMenus(topHeaderMenu.getNavMenuItems());
            }

            Long footerMenuRefId = qlMenuPosByLanguageCode.getFooterMenuRefId();
            if (footerMenuRefId != null) {
                NavMenu footerMenu = navMenuRepository.findOne(footerMenuRefId);
                baseModel.setFooterMenus(footerMenu.getNavMenuItems());
            }
        }

        QlConfig qlAnalyticsConfig = qlConfigService.findOneByKeyOptional("ql-analytics");
        if (qlAnalyticsConfig != null) {
            baseModel.setAnalytics(qlAnalyticsConfig.getValue());
        }

        List<Language> activeLanguages = languageRepository.findAllByActive(true);

        if (!language.equals("en")) {
            // needs translation
            // find translations for active languages
            List<LanguageTranslation> languageTranslations = languageTranslationRepository.
                    findAllByLanguageCodeInAndDisplayLanguageCode(getLanguageCodes(activeLanguages), language);
            List<ActiveLanguageBean> activeLanguageBeans = makeActiveLanguagesForTranslations(activeLanguages, languageTranslations);
            baseModel.setActiveLanguages(activeLanguageBeans);
        } else {
            baseModel.setActiveLanguages(makeActiveLanguageBeansNoTranslation(activeLanguages));
        }

        baseModel.setJsTranslations(getJsTranslations(locale));

        baseModel.setBaseUrl(qlConfigService.findOneByKey("base-url").getValue());

        return baseModel;
    }

    private List<ActiveLanguageBean> makeActiveLanguageBeansNoTranslation(List<Language> activeLanguages) {
        List<ActiveLanguageBean> activeLanguageBeans = new ArrayList<>();

        for (Language activeLanguage : activeLanguages) {
            ActiveLanguageBean activeLanguageBean = new ActiveLanguageBean();
            activeLanguageBean.setLanguage(activeLanguage);
            activeLanguageBeans.add(activeLanguageBean);
        }
        return activeLanguageBeans;
    }

    private List<ActiveLanguageBean> makeActiveLanguagesForTranslations(List<Language> activeLanguages, List<LanguageTranslation> languageTranslations) {
        List<ActiveLanguageBean> activeLanguageBeans = new ArrayList<>();

        for (Language activeLanguage : activeLanguages) {
            for (LanguageTranslation languageTranslation : languageTranslations) {
                if (activeLanguage.getCode().equals(languageTranslation.getLanguageCode())) {
                    ActiveLanguageBean activeLanguageBean = new ActiveLanguageBean();
                    activeLanguageBean.setLanguage(activeLanguage);
                    activeLanguageBean.setLanguageTranslation(languageTranslation);
                    activeLanguageBeans.add(activeLanguageBean);
                }
            }
        }

        return activeLanguageBeans;
    }

    private List<String> getLanguageCodes(List<Language> languages) {
        List<String> result = new ArrayList<>();

        for (Language language : languages) {
            result.add(language.getCode());
        }

        return result;
    }

    private QlMenuPosConfig findQlMenuPosByLanguageCode(String languageCode, List<QlMenuPosConfig> qlMenuPosConfigs) {
        for (QlMenuPosConfig qlMenuPosConfig : qlMenuPosConfigs) {
            if (qlMenuPosConfig.getLanguageCode().equals(languageCode)) {
                return qlMenuPosConfig;
            }
        }
        return null;
    }

    protected String redirectToPageNotFound() throws UnsupportedEncodingException {
        return REDIRECT + URLEncoder.encode("page-not-found", "UTF-8");
    }

    protected Map<String, String> getJsTranslations(Locale locale) {
        JsTranslations jsTranslations = new JsTranslations();
        jsTranslations.addTranslation("page.my_listings.edit_listing.notifications.publish_validation.title", messageSource.getMessage("page.my_listings.edit_listing.notifications.publish_validation.title", null, locale));
        jsTranslations.addTranslation("page.my_listings.edit_listing.notifications.publish_validation.message", messageSource.getMessage("page.my_listings.edit_listing.notifications.publish_validation.message", null, locale));
        jsTranslations.addTranslation("rest.general.upload_success", messageSource.getMessage("rest.general.upload_success", null, locale));
        jsTranslations.addTranslation("rest.general.save_success", messageSource.getMessage("rest.general.save_success", null, locale));
        jsTranslations.addTranslation("info.save_success", messageSource.getMessage("info.save_success", null, locale));
        jsTranslations.addTranslation("info.general_server_error", messageSource.getMessage("info.general_server_error", null, locale));

        return jsTranslations.getTranslations();
    }

    protected void fillListingLocationsMode(ModelMap modelMap, DlListingDTO dlListingDTO, String language) {
        List<DlLocationDTO> dlLocations = dlListingDTO.getDlLocations();
        if (!CollectionUtils.isEmpty(dlLocations)) {
            DlLocationDTO dlLocationDTO = dlLocations.get(0);
            modelMap.addAttribute("dlLocationStates", dlLocationService.findAllByParentId(dlLocationDTO.getParent().getParent().getId(), language));
            modelMap.addAttribute("dlLocationCities", dlLocationService.findAllByParentId(dlLocationDTO.getParent().getId(), language));
        } else {
            modelMap.addAttribute("dlLocationStates", Collections.emptyList());
            modelMap.addAttribute("dlLocationCities", Collections.emptyList());
        }
    }
}
