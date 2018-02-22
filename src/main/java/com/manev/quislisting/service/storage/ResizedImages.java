package com.manev.quislisting.service.storage;

import java.awt.image.BufferedImage;

public class ResizedImages {
    private BufferedImage small;
    private BufferedImage original;

    public BufferedImage getSmall() {
        return small;
    }

    public void setSmall(BufferedImage small) {
        this.small = small;
    }

    public BufferedImage getOriginal() {
        return original;
    }

    public void setOriginal(BufferedImage original) {
        this.original = original;
    }
}
