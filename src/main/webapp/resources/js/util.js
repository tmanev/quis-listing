QlUtil = {
    UI: {
        btnStartLoading: function (btn) {
            let dataLoadingText = btn.attr('data-loading-text');
            let text = btn.html();
            btn.attr('data-original-text', text);
            btn.html(dataLoadingText);
        },
        btnStopLoading: function (btn) {
            let dataOriginalText = btn.attr('data-original-text');
            btn.html(dataOriginalText);
        },
        Notification: {
            showSuccess: function (msgObj) {
                $.notify(msgObj, {
                    type: 'success',
                    placement: {
                        from: "top",
                        align: "center"
                    }
                });
            },
            showError: function (msgObj) {
                $.notify(msgObj, {
                    type: 'danger',
                    placement: {
                        from: "top",
                        align: "center"
                    }
                });
            }
        }
    },
    Rest: {
        authorizationBearer: function () {
            return 'Bearer ' + Cookies.get('ql-auth').split(":")[1];
        },
        Location: {
            getLocations: function (params) {
                return axios.get('/api/dl-locations', {
                    params: params,
                    headers: {
                        'Authorization': QlUtil.Rest.authorizationBearer()
                    }});
            }
        },
        Listing: {
            search: function (params) {
                return axios.get('/api/dl-listings/_search', {
                    params: params,
                    headers: {
                        'Authorization': QlUtil.Rest.authorizationBearer()
                    }
                });
            }
        }
    }
};