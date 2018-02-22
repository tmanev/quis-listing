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
                filter: {
                    text: ''
                },
                dlListings: []
            },
            validations: {},
            methods: {
                onLoadNext: function () {
                    this.pagingParams.isLoading = true;
                    let $btn = $('#btnLoadMore');
                    QlUtil.UI.btnStartLoading($btn);
                    this.$http({
                        params: {
                            page: this.pagingParams.page,
                            size: this.pagingParams.itemsPerPage,
                            languageCode: Cookies.get('ql-lang-key')
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
                        QlUtil.UI.btnStopLoading($btn);
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.pagingParams.isLoading = false;
                        QlUtil.UI.btnStopLoading($btn);
                    });
                },
                onSearch: function (event) {
                    if (this.filter.text !== '') {
                        var filter = {};
                        filter.text = this.filter.text;
                        let params = {
                            query: JSON.stringify(filter)
                        };
                        document.location.href='/search?query=' + encodeURIComponent(params.query);
                    }

                },
            },
            created: function() {

            }
        });

    }
};