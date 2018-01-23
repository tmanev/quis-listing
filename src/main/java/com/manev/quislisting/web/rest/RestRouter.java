package com.manev.quislisting.web.rest;

public final class RestRouter {

    public static final String CONTACTS = "/api/contacts";

    public class DlLocation {

        public static final String LIST = "/api/dl-locations";

        private DlLocation() {
        }
    }

    public class DlContentField {

        public static final String LIST = "/api/dl-content-fields";

        private DlContentField() {
        }
    }

    public class DlListing {
        public static final String LIST = "/api/dl-listings";
        public static final String DETAIL = "/api/dl-listings/{id}";
        public static final String ATTACHMENT_DETAIL = "/api/dl-listings/{id}/attachments/{attachmentId}";
        public static final String PUBLISH = "/api/dl-listings/publish";
        public static final String UPLOAD = "/api/dl-listings/{id}/upload";
        public static final String ACTIVE_LANGUAGES = "/api/dl-listings/active-languages";
        public static final String RECENT = "/api/dl-listings/recent";
        public static final String SEARCH = "/api/dl-listings/_search";

        private DlListing() {
        }
    }

    private RestRouter() {
    }

}
