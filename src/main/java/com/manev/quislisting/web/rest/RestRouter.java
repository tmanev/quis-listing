package com.manev.quislisting.web.rest;

public final class RestRouter {

    private static final String RESOURCE_API_BASE = "/api";
    private static final String RESOURCE_PUBLIC = "/public";
    public static final String RESOURCE_API_NAV_MENUS = RESOURCE_API_BASE + "/nav-menus";

    private static final String RESOURCE_ADMIN = "/admin";
    public static final String RESOURCE_API_ADMIN_DL_CONTENT_FIELDS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-content-fields";
    public static final String RESOURCE_API_ADMIN_DL_CONTENT_FIELD_ITEMS = RESOURCE_API_ADMIN_DL_CONTENT_FIELDS + "/{dlContentFieldId}" + "/dl-content-field-items";

    public static final String RESOURCE_API_ADMIN_ATTACHMENTS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/attachments";
    public static final String RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/content-field-groups";
    public static final String RESOURCE_API_ADMIN_DL_CATEGORIES = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-categories";
    public static final String RESOURCE_API_ADMIN_DL_LOCATIONS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-locations";
    public static final String RESOURCE_API_ADMIN_NAV_MENUS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/nav-menus";
    public static final String RESOURCE_API_ADMIN_LANGUAGES = RESOURCE_API_BASE + RESOURCE_ADMIN + "/languages";
    public static final String RESOURCE_API_ADMIN_QL_CONFIG = RESOURCE_API_BASE + RESOURCE_ADMIN + "/ql-configurations";
    public static final String RESOURCE_API_ADMIN_EMAIL_TEMPLATE = RESOURCE_API_BASE + RESOURCE_ADMIN + "/email-templates";
    public static final String RESOURCE_API_ADMIN_QL_STRINGS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/ql-strings";
    public static final String RESOURCE_API_ADMIN_QL_PAGES = RESOURCE_API_BASE + RESOURCE_ADMIN + "/ql-pages";

    private static final String RESOURCE_CLIENT = "/client";
    public static final String RESOURCE_API_USER_UPLOAD = RESOURCE_API_BASE + RESOURCE_CLIENT + "/upload";
    public static final String RESOURCE_API_CONTACTS = RESOURCE_API_BASE + "/contacts";
    public static final String RESOURCE_API_PUBLIC_DL_LISTINGS = RESOURCE_API_BASE + RESOURCE_PUBLIC + "/dl-listings";
    public static final String RESOURCE_API_PUBLIC_DL_CONTENT_FIELDS = RESOURCE_API_BASE + RESOURCE_PUBLIC + "/dl-content-fields";
    public static final String RESOURCE_API_DL_LISTINGS = RESOURCE_API_BASE + "/dl-listings";
    public static final String RESOURCE_API_ADMIN_DL_LISTINGS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-listings";
    public static final String RESOURCE_API_DL_LOCATIONS = RESOURCE_API_BASE + "/dl-locations";

    public class Rest {

        private Rest() {
            // private constructor
        }

        public class DlContentFieldGroup {

            private DlContentFieldGroup() {
                // private constructor
            }

            public static final String BASE = "/api/admin/dl-content-field-groups";

        }
    }

    public class Mvc {
        public class Listings {
            private Listings() {
                // private constructor
            }

            public static final String BASE = "/listings";
            public static final String VIEW = BASE + "/{id}/{slug}";
        }

        public class MyListings {

            private MyListings() {
            }

            public static final String BASE = "/my-listings";
            public static final String ADD = BASE + "/add";
            public static final String EDIT = BASE + "/{id}";
            public static final String PREVIEW = BASE + "/{id}/preview";
            public static final String PUBLISH_REQUEST_SUCCESS = BASE + "/{id}/publish-successful";
        }
    }

    private RestRouter() {
    }

}
