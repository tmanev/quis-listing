MyListings = {
    init: function () {
        Vue.use(window.vuelidate.default);

        var myListingsApp = new Vue({
            el: '#myListingsApp',
            data: {
                tableParams: {
                    page: {
                        value: '1'
                    },
                    sort: {
                        value: 'id,desc'
                    },
                    search: null,
                    url: '/api/dl-listings?page&sort&search'
                },
                dlListings: []
            },
            validations: {},
            methods: {
                deleteListing: function (listing) {
                    this.$http({
                        url: '/api/dl-listings/' + listing.id,
                        method: 'DELETE'
                    }).then(function (response) {
                        console.log('Success!:', response.data);

                        let index = this.dlListings.indexOf(listing);
                        this.dlListings.splice(index, 1);
                        $.notify({
                            message: response.headers.get('X-qlService-alert')
                        }, {
                            type: 'success'
                        });
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                    });
                }
            },
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