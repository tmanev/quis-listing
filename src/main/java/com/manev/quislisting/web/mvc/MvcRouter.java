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

    public class Social {
        public static final String SIGN_UP = "/social/sign-up";

        private Social() {}
    }

    public class Listings {
        public static final String VIEW = "/listings/{id}/{slug}";

        private Listings() {
        }
    }

    public class MyListings {
        public static final String LIST = "/my-listings";
        public static final String ADD_LISTING_STEP_1_NEW = "/my-listings/add-listing-step-1";
        public static final String ADD_LISTING_STEP_1 = "/my-listings/{id}/add-listing-step-1";
        public static final String ADD_LISTING_STEP_2 = "/my-listings/{id}/add-listing-step-2";
        public static final String ADD_LISTING_STEP_3 = "/my-listings/{id}/add-listing-step-3";
        public static final String ADD_LISTING_STEP_4 = "/my-listings/{id}/add-listing-step-4";
        public static final String EDIT = "/my-listings/{id}";
        public static final String PREVIEW = "/my-listings/{id}/preview";
        public static final String PUBLISH_REQUEST_SUCCESS = "/my-listings/{id}/publish-successful";

        private MyListings() {
        }
    }

    public class MessageCenter {
        public static final String CONVERSATIONS = "/message-center/conversations";
        public static final String CONVERSATION_THREAD = "/message-center/conversations/{messageOverviewId}/thread";

        private MessageCenter() {
        }
    }

    public class Account {
        public static final String PROFILE = "/account/profile";

        private Account() {
        }
    }
}
