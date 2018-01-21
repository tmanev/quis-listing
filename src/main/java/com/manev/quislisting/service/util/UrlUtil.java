package com.manev.quislisting.service.util;

import com.manev.quislisting.web.rest.RestRouter;

public class UrlUtil {

    private UrlUtil() {
        // hide public constructor
    }

    public static String makePublicListingUrl(String baseUrl, Long id, String slug) {
        return baseUrl + RestRouter.Mvc.Listings.VIEW.replace("{id}", id.toString()).replace("{slug}", slug);
    }

    public static String makePreviewListingUrl(String baseUrl, Long id) {
        return baseUrl + RestRouter.Mvc.MyListings.PREVIEW.replace("{id}", id.toString());
    }

    public static String makeEditListingUrl(String baseUrl, Long id) {
        return baseUrl + RestRouter.Mvc.MyListings.EDIT.replace("{id}", id.toString());
    }

}
