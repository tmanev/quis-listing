let ListingContactComponent = {
    init: function () {
        Vue.use(window.vuelidate.default);
        const {required} = window.validators;

        Vue.component('listing-contact-component', {
            template: '#listing-contact-component',
            props: {
                listing: Object
            },
            data: function () {
                return this.listing;
            },
            validations: function () {
                return {
                    listing: {}
                };
            },
            methods: {
                isInputInvalid: function () {
                    if (this.$v.$invalid) {
                        this.$v.$touch();
                        return false;
                    }
                    return true;
                }
            }
        });
    }
};
