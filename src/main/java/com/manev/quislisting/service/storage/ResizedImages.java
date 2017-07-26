package com.manev.quislisting.service.storage;

import java.awt.image.BufferedImage;

public class ResizedImages {
    private BufferedImage small;
    private BufferedImage medium;
    private BufferedImage big;

    public BufferedImage getSmall() {
        return small;
    }

    public void setSmall(BufferedImage small) {
        this.small = small;
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
