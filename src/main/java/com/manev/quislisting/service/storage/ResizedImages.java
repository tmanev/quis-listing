package com.manev.quislisting.service.storage;

import java.awt.image.BufferedImage;

public class ResizedImages {
    private BufferedImage thumbnail;
    private BufferedImage medium;
    private BufferedImage big;

    public BufferedImage getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(BufferedImage thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BufferedImage getMedium() {
        return medium;
    }

    public void setMedium(BufferedImage medium) {
        this.medium = medium;
    }

    public BufferedImage getBig() {
        return big;
    }

    public void setBig(BufferedImage big) {
        this.big = big;
    }
}
