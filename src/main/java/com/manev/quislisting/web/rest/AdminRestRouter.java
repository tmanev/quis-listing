package com.manev.quislisting.web.rest;

public class AdminRestRouter {

    public class DlContentFields {
        public static final String LIST = "/api/admin/dl-content-fields";
        public static final String DETAIL = "/api/admin/dl-content-fields/{id}";

        private DlContentFields() {
        }

        public class DlContentFieldItem {
            public static final String LIST = "/api/admin/dl-content-fields/{dlContentFieldId}/dl-content-field-items";
            public static final String DETAIL = "/api/admin/dl-content-fields/{dlContentFieldId}/dl-content-field-items/{id}";

            private DlContentFieldItem() {
            }
        }
    }

}
