Contacts = {
    init: function () {
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
                onSubmit: function () {
                    if (this.$v.contact.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.contact.$touch();
                    } else {
                        var payload = this.contact;
                        this.$http({url: '/api/contacts', body: payload, method: 'POST'}).then(function (response) {
                            console.log('Success!:', response.data);
                        }, function (response) {
                            console.log('Error!:', response.data);
                        });
                        console.log("Skopsko i se e mozno");
                    }
                }
            }
        });
    }
};