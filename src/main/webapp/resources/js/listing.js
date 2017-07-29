Listing = {
    init: function () {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;


        var contactApp = new Vue({
            el: '#listingApp',
            data: {

            },
            validations: {

            },
            methods : {

            }
        });
    }
};