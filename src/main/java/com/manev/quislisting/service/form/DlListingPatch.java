package com.manev.quislisting.service.form;

public class DlListingPatch {
    private DlListingForm value;
    private Path path;

    public DlListingForm getValue() {
        return value;
    }

    public void setValue(DlListingForm value) {
        this.value = value;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
