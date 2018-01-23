package com.manev.quislisting.web.rest;

import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import com.manev.quislisting.web.rest.RestRouter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@RestController
public class DlContentFieldsRest {

    private final LocaleResolver localeResolver;
    private final DlContentFieldService dlContentFieldService;

    public DlContentFieldsRest(LocaleResolver localeResolver, DlContentFieldService dlContentFieldService) {
        this.localeResolver = localeResolver;
        this.dlContentFieldService = dlContentFieldService;
    }

    @GetMapping(RestRouter.DlContentField.LIST)
    public ResponseEntity<List<DlContentFieldDTO>> getDlContentFields(@RequestParam(name = "categoryId") Long categoryId,
                                                                      HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        List<DlContentFieldDTO> dlContentFieldDTOS = dlContentFieldService.findAllByCategoryId(categoryId, locale.getLanguage());
        return new ResponseEntity<>(dlContentFieldDTOS, HttpStatus.OK);
    }

}
