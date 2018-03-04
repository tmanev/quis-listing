Profile = {
    init: function (user, jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        const touchMap = new WeakMap();

        let profileApp = new Vue({
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
                },
                btnSaveProfileLoading: false,
                btnSavePasswordLoading: false
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
                    let vm = this;
                    if (vm.$v.profile.$invalid) {
                        vm.$v.profile.$touch();
                    } else {
                        vm.btnSaveProfileLoading = true;
                        axios.post('/api/account', vm.profile, {
                            headers: {
                                'Authorization': QlUtil.Rest.authorizationBearer()
                            }
                        }).then(function (response) {
                            let headerLocaleKey = "ql-locale-header";
                            let localeCookie = "ql-lang-key";
                            if (response.headers[headerLocaleKey]) {
                                eraseCookie(localeCookie);
                                createCookie(localeCookie, response.headers[headerLocaleKey]);
                            }
                            vm.$v.profile.$reset();
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['info.save_success']});
                            vm.btnSaveProfileLoading = false;

                            function createCookie(name,value,days) {
                                let expires = "";
                                if (days) {
                                    let date = new Date();
                                    date.setTime(date.getTime()+(days*24*60*60*1000));
                                    expires = "; expires="+date.toGMTString();
                                }
                                document.cookie = name + "=" + value + expires + "; path=/";
                            }

                            function eraseCookie(name) {
                                createCookie(name,"",-1);
                            }
                            // trigger successful block
                        }).catch(function (response) {
                            QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                            vm.btnSaveProfileLoading = false;
                        });
                    }
                },
                onSubmitPassword: function (event) {
                    let vm = this;
                    if (vm.$v.password.$invalid) {
                        vm.$v.password.$touch();
                    } else {
                        vm.btnSavePasswordLoading = true;
                        axios.post('/api/account/change_password', vm.password, {
                            headers: {
                                'Authorization': QlUtil.Rest.authorizationBearer()
                            }
                        }).then(function (response) {
                            vm.password = {
                                oldPassword: '',
                                newPassword: '',
                                newPasswordRepeat: ''
                            };
                            vm.$v.password.$reset();
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['info.save_success']});
                            vm.btnSavePasswordLoading = false;
                            // trigger successful block
                        }).catch(function (response) {
                            QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                            vm.btnSavePasswordLoading = false;
                        });
                    }
                }
            }
        });
    }
};