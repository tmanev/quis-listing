LandingPage = {
    init: function (totalDlListings, loadedDlListings, jsTranslations) {
        Vue.use(window.vuelidate.default);

        let landingApp = new Vue({
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
                    let vm = this;
                    vm.pagingParams.isLoading = true;
                    axios.get(vm.pagingParams.url, {
                        params: {
                            page: vm.pagingParams.page,
                            size: vm.pagingParams.itemsPerPage,
                            languageCode: Cookies.get('ql-lang-key')
                        }
                    }).then(function (response) {
                        for (let i = 0; i<response.data.length; i++) {
                            vm.dlListings.push(response.data[i]);
                        }
                        vm.pagingParams.page++;
                        vm.pagingParams.loadedDlListings += response.data.length;
                        vm.pagingParams.isLoading = false;
                    }).catch(function () {
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        vm.pagingParams.isLoading = false;
                    });
                },
                onSearch: function () {
                    if (this.filter.text !== '') {
                        let filter = {};
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