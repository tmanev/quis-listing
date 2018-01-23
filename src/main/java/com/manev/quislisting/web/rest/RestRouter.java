package com.manev.quislisting.web.rest;

public final class RestRouter {

    public static final String DL_CONTENT_FIELDS = "/api/dl-content-fields";

    public static final String CONTACTS = "/api/contacts";

    private static final String RESOURCE_API_BASE = "/api";

    private static final String RESOURCE_ADMIN = "/admin";

    public static final String RESOURCE_API_ADMIN_DL_CATEGORIES = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-categories";
    public static final String RESOURCE_API_ADMIN_DL_LOCATIONS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-locations";
    public static final String RESOURCE_API_ADMIN_NAV_MENUS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/nav-menus";


    public static final String RESOURCE_API_DL_LOCATIONS = RESOURCE_API_BASE + "/dl-locations";

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
