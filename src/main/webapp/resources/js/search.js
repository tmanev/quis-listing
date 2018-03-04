Search = {
    init: function (dlLocationCountries, totalDlListings, loadedDlListings, jsTranslations) {
        var filterInit = {
            text: '',
            categoryId: -1,
            countryId: -1,
            stateId: -1,
            cityId: -1
        };

        var name = 'query';
        var url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)");
        var results = regex.exec(url);
        if (results) {
            var query = decodeURIComponent(results[2].replace(/\+/g, " "));
            var queryObj = JSON.parse(query);

            if (queryObj.text) {
                filterInit.text = queryObj.text;
            }
            if (queryObj.categoryId) {
                filterInit.categoryId = queryObj.categoryId;
            }
            if (queryObj.countryId) {
                filterInit.countryId = queryObj.countryId;
            }
            if (queryObj.stateId) {
                filterInit.stateId = queryObj.stateId;
            }
            if (queryObj.cityId) {
                filterInit.cityId = queryObj.cityId;
            }
        }

        var searchApp = new Vue({
            el: '#searchApp',
            data: {
                pagingParams: {
                    page: 1,
                    sort: null,
                    search: null,
                    itemsPerPage: 8,
                    totalDlListings: totalDlListings,
                    loadedDlListings: loadedDlListings,
                    url: '/api/dl-listings/_search',
                    isLoading: false
                },
                filter: {
                    text: filterInit.text,
                    selectedCategoryId: filterInit.categoryId,
                    selectedCountryId: filterInit.countryId,
                    selectedStateId: filterInit.stateId,
                    selectedCityId: filterInit.cityId
                },
                dlLocationCountries: dlLocationCountries,
                isStateSelectLoading: false,
                isCitySelectLoading: false,
                dlLocationStates: [],
                dlLocationCities: [],
                dlListings: [],
                dlContentFields: []
            },
            watch: {
                'filter.selectedCountryId': function (val, oldVal) {
                    console.log("selected state id change");
                    if (this.filter.selectedCountryId == -1) {
                        this.dlLocationStates = [];
                        this.filter.selectedStateId = -1;
                        this.dlLocationCities = [];
                        this.filter.selectedCityId = -1;
                    } else {
                        this.filter.selectedStateId = -1;
                        this.filter.selectedCityId = -1;
                        this.fetchStates(this.filter.selectedCountryId);
                    }
                },
                'filter.selectedStateId': function (val, oldVal) {
                    console.log("selected state id change");
                    if (this.filter.selectedStateId == -1) {
                        this.dlLocationCities = [];
                        this.filter.selectedCityId = -1;
                    } else {
                        this.filter.selectedCityId = -1;
                        this.fetchCities(this.filter.selectedStateId);
                    }
                },
                'filter.selectedCityId': function (val, oldVal) {
                            console.log("selected city id change");
                }
            },
            validations: {},
            methods: {
                onLoadNext: function () {
                    let vm = this;
                    this.pagingParams.isLoading = true;
                    let $btn = $('#btnLoadMore');
                    QlUtil.UI.btnStartLoading($btn);
                    let query = this.getQuery();
                    let params = {
                        query: encodeURIComponent(query),
                        page: this.pagingParams.page,
                        size: this.pagingParams.itemsPerPage,
                        languageCode: Cookies.get('ql-lang-key')
                    };
                    QlUtil.Rest.Listing.search(params).then(function (response) {
                        console.log('Success!:', response.data);
                        for (let i = 0; i<response.data.length; i++) {
                            vm.dlListings.push(response.data[i]);
                        }
                        vm.pagingParams.page++;
                        vm.pagingParams.loadedDlListings += response.data.length;
                        vm.pagingParams.isLoading = false;
                        QlUtil.UI.btnStopLoading($btn);
                    }).catch(function (response) {
                        console.log('Error!:', response.data);
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        vm.pagingParams.isLoading = false;
                        QlUtil.UI.btnStopLoading($btn);
                    });
                },
                onSearch: function (event) {
                    let $btn = $('#searchButton');
                    QlUtil.UI.btnStartLoading($btn);
                    let query = this.getQuery();
                    document.location.search='query=' + encodeURIComponent(query);
                },
                onCategoryChange: function (event) {
                    // if (this.selectedCategoryId!==-1) {
                    //     console.log(this.selectedCategoryId);
                    //     let params = {
                    //         categoryId: this.selectedCategoryId
                    //     };
                    //     this.$http({
                    //         url: '/api/dl-content-fields',
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
                fetchStates: function (parentId) {
                    var params = {
                        parentId: parentId,
                        languageCode: Cookies.get('ql-lang-key')
                    };
                    this.isStateSelectLoading = true;
                    this.$http({url: '/api/dl-locations', params: params, method: 'GET'}).then(function (response) {
                        console.log('Success!:', response.data);
                        this.dlLocationStates = response.data;
                        this.isStateSelectLoading = false;
                    }, function (response) {
                        console.log('Error!:', response.data);
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        this.isStateSelectLoading = false;
                    });
                },
                fetchCities: function (parentId) {
                    var params = {
                        parentId: parentId,
                        languageCode: Cookies.get('ql-lang-key')
                    };
                    this.isCitySelectLoading = true;
                    this.$http({url: '/api/dl-locations', params: params, method: 'GET'}).then(function (response) {
                        console.log('Success!:', response.data);
                        this.dlLocationCities = response.data;
                        this.isCitySelectLoading = false;
                    }, function (response) {
                        console.log('Error!:', response.data);
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        this.isCitySelectLoading = false;
                    });
                },
                getQuery: function () {
                    let filter = {};
                    filter.text = this.filter.text;
                    if (this.filter.selectedCategoryId != -1) {
                        filter.categoryId = this.filter.selectedCategoryId;
                    }
                    if (this.filter.selectedCityId != -1) {
                        filter.cityId = this.filter.selectedCityId;
                    }
                    if (this.filter.selectedStateId != -1) {
                        filter.stateId = this.filter.selectedStateId;
                    }
                    if (this.filter.selectedCountryId != -1) {
                        filter.countryId = this.filter.selectedCountryId;
                    }

                    return JSON.stringify(filter)
                }
            },
            mounted: function () {
                if (filterInit.countryId != -1) {
                    this.fetchStates(filterInit.countryId);
                }
                if (filterInit.stateId != -1) {
                    this.fetchCities(filterInit.stateId);
                }

            }
        });
    }
};