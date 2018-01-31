Contacts = {
    init: function (jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;


        var contactApp = new Vue({
            el: '#contactApp',
            data: {
                contact: {
                    name: '',
                    email: '',
                    subject: '',
                    message: ''
                }
            },
            validations: {
                contact:{
                    name: {
                        required: required
                    },
                    email: {
                        required: required,
                        email: email
                    }
                }
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
                                message: ''
                            };
                            this.$v.contact.$reset();
                            $.notify({
                                message: jsTranslations['page.contact.message.sent_success']
                            },{
                                type: 'success'
                            });
                            $btn.button('reset');
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            },{
                                type: 'danger'
                            });
                            $btn.button('reset');
                        });

                    }
                }
            }
        });
    }
};