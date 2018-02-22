SignUp = {
    init: function (signUpUserBean) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email} = window.validators;


        var signUpApp = new Vue({
            el: '#signUpApp',
            data: {
                signUp: {
                    email: signUpUserBean.email,
                    firstName: signUpUserBean.firstName,
                    lastName: signUpUserBean.lastName,
                    updates: signUpUserBean.updates,
                    password: signUpUserBean.password
                }
            },
            validations: {
                signUp:{
                    email: {
                        required: required,
                        email: email,
                        minLength: minLength(5),
                        maxLength: maxLength(100)
                    },
                    firstName: {
                        required: required,
                        maxLength: maxLength(50)
                    },
                    lastName: {
                        required: required,
                        maxLength: maxLength(50)
                    },
                    password: {
                        required: required,
                        minLength: minLength(6),
                        maxLength: maxLength(100)
                    }
                }
            },
            methods : {
                onSubmit: function (event) {
                    if (this.$v.signUp.$invalid) {
                        console.log("Please fill the required fields!");
                        // show notification

                        this.$v.signUp.$touch();
                    } else {
                        console.log("valid");
                        var $form = $('#signUp');
                        $form.get(0).submit();
                    }
                }
            }
        });
    }
};