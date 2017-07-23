package com.manev.quislisting.service.dto;

public class AttachmentMetadata {
    private DetailSize detail;
    private ImageResizeMeta smallImageResizeMeta;
    private ImageResizeMeta mediumImageResizeMeta;
    private ImageResizeMeta bigImageResizeMeta;

    public DetailSize getDetail() {
        return detail;
    }

    public void setDetail(DetailSize detail) {
        this.detail = detail;
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

    public ImageResizeMeta getSmallImageResizeMeta() {
        return smallImageResizeMeta;
    }

    public void setSmallImageResizeMeta(ImageResizeMeta thumbnailImageResizeMeta) {
        this.smallImageResizeMeta = thumbnailImageResizeMeta;
    }

    public ImageResizeMeta getMediumImageResizeMeta() {
        return mediumImageResizeMeta;
    }

    public void setMediumImageResizeMeta(ImageResizeMeta mediumImageResizeMeta) {
        this.mediumImageResizeMeta = mediumImageResizeMeta;
    }

    public ImageResizeMeta getBigImageResizeMeta() {
        return bigImageResizeMeta;
    }

    public void setBigImageResizeMeta(ImageResizeMeta bigImageResizeMeta) {
        this.bigImageResizeMeta = bigImageResizeMeta;
    }
}
