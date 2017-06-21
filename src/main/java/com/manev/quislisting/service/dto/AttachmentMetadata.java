package com.manev.quislisting.service.dto;

import java.util.ArrayList;
import java.util.List;

public class AttachmentMetadata {
    private DetailSize detail;
    private List<ImageResizeMeta> imageResizeMetas;

    public DetailSize getDetail() {
        return detail;
    }

    public void setDetail(DetailSize detail) {
        this.detail = detail;
    }

    public List<ImageResizeMeta> getImageResizeMetas() {
        return imageResizeMetas;
    }

    public void setImageResizeMetas(List<ImageResizeMeta> imageResizeMetas) {
        this.imageResizeMetas = imageResizeMetas;
    }

    public ImageResizeMeta getImageResizeMetaByName(String name) {
        if (imageResizeMetas != null) {
            for (ImageResizeMeta imageResizeMeta : imageResizeMetas) {
                if (name.equals(imageResizeMeta.getName())) {
                    return imageResizeMeta;
                }
            }
        }
        return null;
    }

    public void addSize(ImageResizeMeta imageResizeMeta) {
        if (this.imageResizeMetas == null) {
            imageResizeMetas = new ArrayList<>();
        }
        this.imageResizeMetas.add(imageResizeMeta);
    }

    public static class ImageResizeMeta {
        private String name;
        private DetailSize detail;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DetailSize getDetail() {
            return detail;
        }

        public void setDetail(DetailSize detail) {
            this.detail = detail;
        }
    }

    public static class DetailSize {
        private String file;
        private Integer width;
        private Integer height;
        private Long size;
        private String mimeType;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
    }
}
