PasswordResetFinish = {
    init: function (jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        const touchMap = new WeakMap();

        var passwordResetFinishApp = new Vue({
            el: '#passwordResetFinishApp',
            data: {
                form: {
                    password: '',
                    repeatPassword: ''
                },
                success: false
            },
            validations: {
                form:{
                    password: {
                        required: required,
                        minLength: minLength(6),
                        maxLength: maxLength(100)
                    },
                    repeatPassword: {
                        sameAsPassword: sameAs('password')
                    }
                }
            },
            methods : {
                delayTouch: function ($v) {
                    $v.$reset();
                    if (touchMap.has($v)) {
                        clearTimeout(touchMap.get($v));
                    }
                    touchMap.set($v, setTimeout($v.$touch, 1000))
                },
                onSubmit: function (event) {
                    if (this.$v.form.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.form.$touch();
                    } else {
                        console.log("valid");
                        let key = getParameterByName('key');
                        var payload = {
                            key: key,
                            newPassword: this.form.password
                        };
                        var $btn = $('#btnResetPassword').button('loading');
                        this.$http({
                            url: '/api/account/reset_password/finish',
                            body: payload,
                            method: 'POST'
                        }).then(function (response) {
                            console.log('Success!:', response.data);
                            this.form = {
                                password: '',
                                repeatPassword: ''
                            };
                            this.$v.form.$reset();
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['info.save_success']});
                            $btn.button('reset');
                            this.success = true;
                            // trigger successful block
                        }, function (response) {
                            console.log('Error!:', response.data);
                            QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                            $btn.button('reset');
                        });
                    }
                }
            }
        });

        function getParameterByName(name, url) {
            if (!url) url = window.location.href;
            name = name.replace(/[\[\]]/g, "\\$&");
            var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, " "));
        }
    }
};