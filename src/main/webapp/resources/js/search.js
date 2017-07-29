Search = {
    init: function () {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;


        var searchApp = new Vue({
            el: '#searchApp',
            data: {
                search: "",
                dlListings: []
            },
            validations: {},
            methods: {
                onSearch: function (event) {
                    var $btn = $('#searchButton').button('loading');

                    let params = {
                        query: this.search
                    };
                    this.$http(
                        {
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
                }
            }
        });
    }
};