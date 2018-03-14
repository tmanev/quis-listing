Listing = {
    init: function (listingId, isAuthenticated, jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email} = window.validators;

        let vm = new Vue({
            el: '#listingApp',
            data: {
                listingId: listingId,
                isAuthenticated: isAuthenticated,
                dlMessage: {
                    senderName: '',
                    senderEmail: '',
                    text: '',
                    languageCode: Cookies.get('ql-lang-key')
                },
                btnSendMessageLoading: false,
                tabIndex: 0
            },
            validations: function () {
                if (this.isAuthenticated) {
                    return {
                        dlMessage: {
                            senderName: {
                                required: false
                            },
                            senderEmail: {
                                required: false
                            },
                            text: {
                                required: required
                            }
                        }
                    }
                } else {
                    return {
                        dlMessage: {
                            senderName: {
                                required: required
                            },
                            senderEmail: {
                                required: required,
                                email: email,
                                minLength: minLength(5),
                                maxLength: maxLength(100)
                            },
                            text: {
                                required: required
                            }
                        }
                    }
                }
            },
            methods: {
                onSendMessage: function () {
                    if (vm.$v.dlMessage.$invalid) {
                        vm.$v.dlMessage.$touch();
                    } else {
                        vm.btnSendMessageLoading = true;
                        let payload = vm.dlMessage;
                        axios.post('/api/dl-listings/' + vm.listingId + '/messages', payload, {
                            headers: {
                                'Authorization': QlUtil.Rest.authorizationBearer()
                            }
                        }).then(function () {
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['page.listing.label.send_message.success']});
                            vm.btnSendMessageLoading = false;
                            vm.dlMessage.senderName = '';
                            vm.dlMessage.senderEmail = '';
                            vm.dlMessage.text = '';
                        }, function () {
                            QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                            vm.btnSendMessageLoading = false;
                        });
                    }
                }
            }
        });

    }
};
