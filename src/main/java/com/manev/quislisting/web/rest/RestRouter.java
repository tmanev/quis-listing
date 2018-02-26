package com.manev.quislisting.web.rest;

public final class RestRouter {

    public class Account {
        public static final String BASE = "/api/account";
        public static final String MOBILE_BASE = "/api/mobile/account";
        public static final String CHANGE_PASS = "/api/account/change_password";
        public static final String RESET_PASS_INIT = "/api/account/reset_password/init";
        public static final String RESET_PASS_FINISH = "/api/account/reset_password/finish";

        private Account() {
        }
    }

    public class User {
        public static final String REGISTER = "/api/register";
        public static final String ACTIVATE = "/api/activate";
        public static final String AUTHENTICATE = "/api/authenticate";

        private User() {
        }
    }

    public class Application {

        public static final String PROFILE = "/api/profile-info";

        private Application() {
        }
    }

    public class Contact {
        public static final String BASE = "/api/contacts";

        private Contact() {
        }
    }

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
