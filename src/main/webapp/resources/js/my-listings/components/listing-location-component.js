var ListingLocationComponent = {
    init: function (dlListingDTO, dlLocationCountries, dlLocationStates, dlLocationCities) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        let selectedCityId = -1;
        let selectedStateId = -1;
        let selectedCountryId = -1;
        if (dlListingDTO.dlLocations && dlListingDTO.dlLocations.length > 0) {
            selectedCityId = dlListingDTO.dlLocations[0].id;
            selectedStateId = dlListingDTO.dlLocations[0].parentId;
            selectedCountryId = dlListingDTO.dlLocations[0].parent.parent.id;
        }

        Vue.component('listing-location-component', {
            template: '#listing-location-component',
            props: {
                listing: Object
            },
            data: function () {
                return {
                    isStateSelectLoading: false,
                    isCitySelectLoading: false,
                    selectedCountry: selectedCountryId,
                    selectedState: selectedStateId,
                    selectedCity: selectedCityId,
                    dlLocationCountries: dlLocationCountries,
                    dlLocationStates: dlLocationStates,
                    dlLocationCities: dlLocationCities,
                }
            },
            validations: function () {
                return {
                    selectedCity: {
                        ListHasSelectionValidator: listHasSelectionValidator
                    }
                };
            },
            watch: {
                'selectedCity': function (val, oldVal) {
                    if (this.selectedCity != "-1") {
                        this.listing.dlLocations = [{
                            id: this.selectedCity
                        }];
                    } else {
                        this.listing.dlLocations = [];
                    }
                }
            },
            methods: {
                isInputInvalid: function () {
                    if (this.$v.$invalid) {
                        this.$v.$touch();
                        return false;
                    }
                    return true;
                },
                onCountryChange: function () {
                    let vm = this;
                    if (this.selectedCountry === "-1") {
                        this.dlLocationStates = [];
                        this.selectedState = -1;
                        this.dlLocationCities = [];
                        this.selectedCity = -1;
                    } else {
                        let params = {
                            parentId: this.selectedCountry,
                            languageCode: Cookies.get('ql-lang-key')
                        };
                        this.isStateSelectLoading = true;
                        this.selectedState = -1;
                        this.selectedCity = -1;
                        QlUtil.Rest.Location.getLocations(params).then(function (response) {
                            console.log('Success!:', response.data);
                            vm.dlLocationStates = response.data;
                            vm.isStateSelectLoading = false;
                        }).catch(function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            vm.isStateSelectLoading = false;
                        });
                    }
                },
                onStateChange: function () {
                    let vm = this;
                    if (this.selectedState === "-1") {
                        this.dlLocationCities = [];
                        this.selectedCity = -1;
                    } else {
                        let params = {
                            parentId: this.selectedState,
                            languageCode: Cookies.get('ql-lang-key')
                        };
                        this.isCitySelectLoading = true;
                        this.selectedCity = -1;
                        QlUtil.Rest.Location.getLocations(params).then(function (response) {
                            console.log('Success!:', response.data);
                            vm.dlLocationCities = response.data;
                            vm.isCitySelectLoading = false;
                        }).catch(function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            vm.isCitySelectLoading = false;
                        });
                    }
                },
            }
        });
    }
};
