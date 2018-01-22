LandingPage = {
    init: function (totalDlListings, loadedDlListings) {
        Vue.use(window.vuelidate.default);

        var landingApp = new Vue({
            el: '#landingApp',
            data: {
                pagingParams: {
                    page: 1,
                    sort: null,
                    search: null,
                    itemsPerPage: 12,
                    totalDlListings: totalDlListings,
                    loadedDlListings: loadedDlListings,
                    url: '/api/dl-listings/recent',
                    isLoading: false
                },
                dlListings: []
            },
            validations: {},
            methods: {
                onLoadNext: function () {
                    this.pagingParams.isLoading = true;
                    var $btn = $('#btnLoadMore').button('loading');
                    this.$http({
                        params: {
                            page: this.pagingParams.page,
                            size: this.pagingParams.itemsPerPage
                        },
                        url: this.pagingParams.url,
                        method: 'GET'
                    }).then(function (response) {
                        console.log('Success!:', response.data);
                        for (var i = 0; i<response.data.length; i++) {
                            this.dlListings.push(response.data[i]);
                        }
                        this.pagingParams.page++;
                        this.pagingParams.loadedDlListings += response.data.length;
                        this.pagingParams.isLoading = false;
                        $btn.button('reset');
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.pagingParams.isLoading = false;
                        $btn.button('reset');
                    });
                }
            },
            created: function() {

            }
        });

    }
};