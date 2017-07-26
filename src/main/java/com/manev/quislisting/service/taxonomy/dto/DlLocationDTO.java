package com.manev.quislisting.service.taxonomy.dto;

public class DlLocationDTO extends TermTaxonomyDTO {

    private DlLocationDTO parent;

    public DlLocationDTO getParent() {
        return parent;
    }

    public void setParent(DlLocationDTO parent) {
        this.parent = parent;
    }
}
