ForgotPassword = {
    init: function () {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;

        const touchMap = new WeakMap();

        var forgotPasswordApp = new Vue({
            el: '#forgotPasswordApp',
            data: {
                forgotPassword: {
                    email: ''
                }
            },
            validations: {
                forgotPassword: {
                    email: {
                        required: required,
                        email: email
                    }
                }
            },
            methods: {
                delayTouch: function ($v) {
                    $v.$reset();
                    if (touchMap.has($v)) {
                        clearTimeout(touchMap.get($v));
                    }
                    touchMap.set($v, setTimeout($v.$touch, 1000))
                },
                onSubmit: function (event) {
                    if (this.$v.forgotPassword.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.forgotPassword.$touch();
                    } else {
                        var payload = this.forgotPassword.email;
                        var $btn = $('#sendButton').button('loading');
                        this.$http({
                            url: 'api/account/reset_password/init',
                            body: payload,
                            method: 'POST'
                        }).then(function (response) {
                            console.log('Success!:', response.data);
                            this.forgotPassword = {
                                email: ''
                            };
                            this.$v.forgotPassword.$reset();
                            $.notify({
                                message: response.data
                            }, {
                                type: 'success'
                            });
                            $btn.button('reset');
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
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