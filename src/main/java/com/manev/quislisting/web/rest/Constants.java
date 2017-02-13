package com.manev.quislisting.web.rest;

public final class Constants {

    private static final String RESOURCE_API_BASE = "/api";
    public static final String RESOURCE_API_POST_CATEGORIES = RESOURCE_API_BASE + "/post-categories";
    public static final String RESOURCE_API_NAV_MENUS = RESOURCE_API_BASE + "/nav-menus";
    public static final String RESOURCE_API_DL_LOCATIONS = RESOURCE_API_BASE + "/dl-locations";
    public static final String RESOURCE_API_DL_CATEGORIES = RESOURCE_API_BASE + "/dl-categories";
    private static final String RESOURCE_ADMIN = "/admin";
    public static final String RESOURCE_API_ADMIN_DL_LISTINGS = RESOURCE_API_BASE + RESOURCE_ADMIN + "/dl-listings";
    public static final String RESOURCE_API_ADMIN_CONTENT_FIELD_GROUPS = RESOURCE_API_BASE + RESOURCE_ADMIN + "content-field-groups";
    private Constants() {
    }

}
