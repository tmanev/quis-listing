AddListingStep4 = {
    init: function (dlListingDTO) {
        Vue.use(window.vuelidate.default);

        const touchMap = new WeakMap();

        let addListingStep4App = new Vue({
            el: '#addListingStep4App',
            data: {
                listing: listingDtoToListingForm(dlListingDTO)
            },
            validations: function () {
                return {};
            },
            methods: {
                delayTouch: function ($v) {
                    $v.$reset();
                    if (touchMap.has($v)) {
                        clearTimeout(touchMap.get($v));
                    }
                    touchMap.set($v, setTimeout($v.$touch, 1000))
                },
                onSubmit: function () {
                    let btn = $('#btnNext');
                    QlUtil.UI.btnStartLoading(btn);
                    let afterPublishUrl = '/my-listings/' + this.listing.id + '/publish-successful';
                    MyListingService.publishListing(this.listing).then(function (response) {
                        QlUtil.UI.btnStopLoading(btn);
                        // move the user to the next page
                        window.location.href = afterPublishUrl;
                    }).catch(function (error) {
                        $.notify({
                            message: error.data
                        }, {
                            type: 'danger'
                        });
                        QlUtil.UI.btnStopLoading(btn);
                    });
                },
                onPrevious: function () {
                    window.location.href = '/my-listings/' + this.listing.id + '/add-listing-step-3';
                }
            }
        });
    }
};