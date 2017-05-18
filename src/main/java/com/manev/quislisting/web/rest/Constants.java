package com.manev.quislisting.web.rest;

public final class Constants {

    private static final String RESOURCE_API_BASE = "/api";
    public static final String RESOURCE_API_POST_CATEGORIES = RESOURCE_API_BASE + "/post-categories";
    public static final String RESOURCE_API_NAV_MENUS = RESOURCE_API_BASE + "/nav-menus";

    private static final String RESOURCE_ADMIN = "/admin";
    public static final String RESOURCE_API_ADMIN_DL_CONTENT_FIELDS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-content-fields";
    public static final String RESOURCE_API_ADMIN_DL_LISTINGS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-listings";
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

    private static final String RESOURCE_USER = "/user";
    public static final String RESOURCE_API_USER_UPLOAD = RESOURCE_API_BASE + RESOURCE_USER + "/upload";
    public static final String RESOURCE_API_CONTACTS = RESOURCE_API_BASE + "/contacts";

    private Constants() {
    }

}
