Contacts = {
    init: function () {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between} = window.validators;


        var contactApp = new Vue({
            el: '#contactApp',
            data: {
                message: 'Hello Vue!',
                text: '',
                name: '',
                age: 0
            },
            validations: {
                text: {
                    required: true,
                    minLength: minLength(5)
                },
                name: {
                    required: true,
                    minLength: minLength(4)
                },
                age: {
                    between: between(20, 30)
                }
            },
            methods : {
                onSubmit: function () {
                    console.log("Skopsko i se e mozno");
                }
            }
        });
    }
};