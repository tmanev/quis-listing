LandingPage = {
    init: function () {
        Vue.use(window.vuelidate.default);

        var landingApp = new Vue({
            el: '#landingApp',
            data: {
                tableParams: {
                    page: {
                        value: '1'
                    },
                    sort: {
                        value: 'id,desc'
                    },
                    search: null,
                    url: '/api/public/dl-listings?page&sort&search'
                },
                dlListings: []
            },
            validations: {},
            methods: {},
            created: function() {
                this.$http({
                    url: this.tableParams.url,
                    method: 'GET'
                }).then(function (response) {
                    console.log('Success!:', response.data);
                    // vm.links = ParseLinks.parse(headers.get('link'));
                    let totalItems = response.headers.get('X-Total-Count');
                    let queryCount = totalItems;
                    this.dlListings = response.data;
                }, function (response) {
                    console.log('Error!:', response.data);
                    $.notify({
                        message: response.data
                    }, {
                        type: 'danger'
                    });
                });
            }
        });

    }
};