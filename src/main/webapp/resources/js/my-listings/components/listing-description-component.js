let ListingDescriptionComponent = {
    init: function () {
        Vue.use(window.vuelidate.default);
        const {required} = window.validators;

        Vue.component('listing-description-component', {
            template: '#listing-description-component',
            props: {
                listing: Object
            },
            data: function () {
                return this.listing;
            },
            validations: function () {
                return {
                    listing: {
                        title: {
                            required: required,
                            TwoWordValidator: twoWordValidator
                        }
                    }
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
