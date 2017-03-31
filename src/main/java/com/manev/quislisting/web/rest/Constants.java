package com.manev.quislisting.web.rest;

public final class Constants {

    private static final String RESOURCE_API_BASE = "/api";
    public static final String RESOURCE_API_POST_CATEGORIES = RESOURCE_API_BASE + "/post-categories";
    public static final String RESOURCE_API_NAV_MENUS = RESOURCE_API_BASE + "/nav-menus";
    private static final String RESOURCE_ADMIN = "/admin";
    public static final String RESOURCE_API_ADMIN_DL_CONTENT_FIELDS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-content-fields";
    public static final String RESOURCE_API_ADMIN_DL_LISTINGS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-listings";
    public static final String RESOURCE_API_ADMIN_ATTACHMENTS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/attachments";
    public static final String RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS = RESOURCE_API_BASE + RESOURCE_ADMIN + "content-field-groups";
    public static final String RESOURCE_API_ADMIN_DL_CATEGORIES = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-categories";
    public static final String RESOURCE_API_ADMIN_DL_LOCATIONS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-locations";
    public static final String RESOURCE_API_ADMIN_LANGUAGES = RESOURCE_API_BASE + RESOURCE_ADMIN + "/languages";

    private static final String RESOURCE_USER = "/user";
    public static final String RESOURCE_API_USER_UPLOAD = RESOURCE_API_BASE + RESOURCE_USER + "/upload";

    private Constants() {
    }

}
