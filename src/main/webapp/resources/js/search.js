Search = {
    init: function (dlLocationCountries) {
        var searchApp = new Vue({
            el: '#searchApp',
            data: {
                text: '',
                selectedCategoryId: "-1",
                selectedCountryId: "-1",
                selectedStateId: "-1",
                selectedCityId: "-1",
                dlLocationCountries: dlLocationCountries,
                isStateSelectLoading: false,
                isCitySelectLoading: false,
                dlLocationStates: [],
                dlLocationCities: [],
                dlListings: [],
                dlContentFields: []
            },
            validations: {},
            methods: {
                onSearch: function (event) {
                    var $btn = $('#searchButton').button('loading');

                    var filter = {};
                    filter.text = this.text;
                    if (this.selectedCategoryId !== "-1") {
                        filter.categoryId = this.selectedCategoryId;
                    }
                    if (this.selectedCityId !== "-1") {
                        filter.locationId = this.selectedCityId;
                    } else if (this.selectedStateId !== "-1") {
                        filter.locationId = this.selectedStateId;
                    } else if (this.selectedCountryId !== "-1") {
                        filter.locationId = this.selectedCountryId;
                    }
                    let params = {
                        query: JSON.stringify(filter)
                    };
                    this.$http({
                            url: '/api/public/dl-listings/_search',
                            method: 'GET',
                            params: params
                        }).then(function (response) {
                        console.log('Success!:', response.data);
                        this.dlListings = response.data;
                        $btn.button('reset');
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        $btn.button('reset');
                    });
                },
                onCategoryChange: function (event) {
                    // if (this.selectedCategoryId!=="-1") {
                    //     console.log(this.selectedCategoryId);
                    //     let params = {
                    //         categoryId: this.selectedCategoryId
                    //     };
                    //     this.$http({
                    //         url: '/api/public/dl-content-fields/',
                    //         method: 'GET',
                    //         params: params
                    //     }).then(function (response) {
                    //         console.log('Success!:', response.data);
                    //         this.dlContentFields = response.data;
                    //     }, function (response) {
                    //         console.log('Error!:', response.data);
                    //         $.notify({
                    //             message: response.data
                    //         }, {
                    //             type: 'danger'
                    //         });
                    //     });
                    // }
                },
                onCountryChange: function () {
                    if (this.selectedCountryId === "-1") {
                        this.dlLocationStates = [];
                        this.selectedStateId = -1;
                        this.dlLocationCities = [];
                        this.selectedCityId = -1;
                    } else {
                        var params = {
                            parentId: this.selectedCountryId
                        };
                        this.isStateSelectLoading = true;
                        this.selectedStateId = -1;
                        this.selectedCityId = -1;
                        this.$http({url: '/api/dl-locations', params: params, method: 'GET'}).then(function (response) {
                            console.log('Success!:', response.data);
                            this.dlLocationStates = response.data;
                            this.isStateSelectLoading = false;
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            this.isStateSelectLoading = false;
                        });
                    }
                },
                onStateChange: function () {
                    if (this.selectedStateId === "-1") {
                        this.dlLocationCities = [];
                        this.selectedCityId = -1;
                    } else {
                        var params = {
                            parentId: this.selectedStateId
                        };
                        this.isCitySelectLoading = true;
                        this.selectedCityId = -1;
                        this.$http({url: '/api/dl-locations', params: params, method: 'GET'}).then(function (response) {
                            console.log('Success!:', response.data);
                            this.dlLocationCities = response.data;
                            this.isCitySelectLoading = false;
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            this.isCitySelectLoading = false;
                        });
                    }
                },
            }
        });
    }
};