Contacts = {
    init: function (jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;


        var contactApp = new Vue({
            el: '#contactApp',
            data: {
                contactWasValidated: false,
                contact: {
                    name: '',
                    email: '',
                    subject: '',
                    message: '',
                    languageCode: Cookies.get('ql-lang-key')
                }
            },
            validations: function() {
                return {
                    contact:{
                        name: {
                            required: required
                        },
                        email: {
                            required: required,
                            email: email
                        }
                    }
                };
            },
            methods : {
                onSubmit: function (event) {
                    if (this.$v.contact.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.contact.$touch();
                    } else {
                        var payload = this.contact;
                        var $btn = $('#sendButton').button('loading');
                        this.$http({url: '/api/contacts', body: payload, method: 'POST'}).then(function (response) {
                            console.log('Success!:', response.data);
                            this.contact = {
                              name: '',
                                email: '',
                                subject: '',
                                message: '',
                                languageCode: Cookies.get('ql-lang-key')
                            };
                            this.$v.contact.$reset();
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['page.contact.message.sent_success']});
                            $btn.button('reset');
                        }, function (response) {
                            QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                            $btn.button('reset');
                        });

                    }
                }
            }
        });
    }
};