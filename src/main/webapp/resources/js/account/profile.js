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
                },
                password: {
                    oldPassword: '',
                    newPassword: '',
                    newPasswordRepeat: ''
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
                },
                password: {
                    oldPassword: {
                        required: required
                    },
                    newPassword: {
                        required: required,
                        minLength: minLength(6),
                        maxLength: maxLength(100)
                    },
                    newPasswordRepeat: {
                        sameAsPassword: sameAs('newPassword')
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
                            var headerLocaleKey = "ql-locale-header";
                            var localeCookie = "ql-lang-key";
                            if (response.headers.get(headerLocaleKey)) {
                                eraseCookie(localeCookie);
                                createCookie(localeCookie, response.headers.get(headerLocaleKey));
                            }
                            this.$v.profile.$reset();
                            $.notify({
                                message: response.bodyText
                            }, {
                                type: 'success'
                            });
                            $btn.button('reset');

                            function createCookie(name,value,days) {
                                if (days) {
                                    var date = new Date();
                                    date.setTime(date.getTime()+(days*24*60*60*1000));
                                    var expires = "; expires="+date.toGMTString();
                                }
                                else var expires = "";
                                document.cookie = name+"="+value+expires+"; path=/";
                            }

                            function eraseCookie(name) {
                                createCookie(name,"",-1);
                            }
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
                },
                onSubmitPassword: function (event) {
                    if (this.$v.password.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.password.$touch();
                    } else {
                        console.log("valid");
                        var $btn = $('#btnSavePassword').button('loading');
                        this.$http({
                            url: '/api/account/change_password',
                            body: this.password,
                            method: 'POST'
                        }).then(function (response) {
                            console.log('Success!:', response.data);

                            this.profile = {
                                oldPassword: '',
                                newPassword: '',
                                newPasswordRepeat: ''
                            };
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