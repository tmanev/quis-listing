package com.manev.quislisting.web.mvc;

public class MvcRouter {

    public static final String HOME = "/";
    public static final String ACCOUNT_ACTIVATE = "/account-activate";
    public static final String CONTACT = "/contact";
    public static final String CONTENT = "/content/files/{year}/{month}/{day}/{filename:.+}";
    public static final String FORGOT_PASS = "/forgot-password";
    public static final String PAGE_NOT_FOUND = "/page-not-found";
    public static final String PASS_RESET = "/password-reset";
    public static final String SEARCH = "/search";
    public static final String SIGN_IN = "/sign-in";
    public static final String SIGN_OUT = "/sign-out";
    public static final String SIGN_UP = "/sign-up";
    public static final String STATIC_PAGE = "/{name}";

    private MvcRouter() {
    }

    public class Listings {
        public static final String VIEW = "/listings/{id}/{slug}";

        private Listings() {
        }
    }

    public class MyListings {

        public static final String BASE = "/my-listings";
        public static final String ADD = BASE + "/add";
        public static final String EDIT = BASE + "/{id}";
        public static final String PREVIEW = BASE + "/{id}/preview";
        public static final String PUBLISH_REQUEST_SUCCESS = BASE + "/{id}/publish-successful";

        private MyListings() {
        }
    }

    public class Account {
        private static final String BASE = "/account";
        public static final String PROFILE = BASE + "/profile";

        private Account() {
        }
    }
}
