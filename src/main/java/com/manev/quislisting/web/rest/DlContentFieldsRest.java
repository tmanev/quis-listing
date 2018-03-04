package com.manev.quislisting.web.rest;

import com.manev.quislisting.service.DlContentFieldService;
import com.manev.quislisting.service.dto.DlContentFieldDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DlContentFieldsRest {

    private final DlContentFieldService dlContentFieldService;

    public DlContentFieldsRest(DlContentFieldService dlContentFieldService) {
        this.dlContentFieldService = dlContentFieldService;
    }

    @GetMapping(RestRouter.DlContentField.LIST)
    public ResponseEntity<List<DlContentFieldDTO>> getDlContentFields(@RequestParam(name = "categoryId") Long categoryId,
                                                                      @RequestParam(required = false, defaultValue = "en") String languageCode) {
        List<DlContentFieldDTO> dlContentFieldDTOS = dlContentFieldService.findAllByCategoryId(categoryId, languageCode);
        return new ResponseEntity<>(dlContentFieldDTOS, HttpStatus.OK);
    }

}
