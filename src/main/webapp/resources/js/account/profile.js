Profile = {
    init: function (user) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        const touchMap = new WeakMap();

        var profileApp = new Vue({
            el: '#profileApp',
            data: {
                profile: {
                    login: user.login,
                    email: user.email,
                    firstName: user.firstName,
                    lastName: user.lastName,
                    updates: user.updates,
                    langKey: user.langKey
                }
            },
            validations: {
                profile: {
                    firstName: {
                        maxLength: maxLength(50)
                    },
                    lastName: {
                        maxLength: maxLength(50)
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
                    if (this.$v.profile.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.profile.$touch();
                    } else {
                        console.log("valid");
                        var $btn = $('#btnSaveProfile').button('loading');
                        this.$http({
                            url: '/api/account',
                            body: this.profile,
                            method: 'POST'
                        }).then(function (response) {
                            console.log('Success!:', response.data);

                            this.$v.profile.$reset();
                            $.notify({
                                message: response.bodyText
                            }, {
                                type: 'success'
                            });
                            $btn.button('reset');
                            // trigger successful block
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