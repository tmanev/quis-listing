AddListingStep3 = {
    init: function (dlListingDTO, jsTranslations) {
        Vue.use(window.vuelidate.default);

        const touchMap = new WeakMap();

        let addListingStep3App = new Vue({
            el: '#addListingStep3App',
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
                    let afterSaveUrl = '/my-listings/' + this.listing.id + '/add-listing-step-4';
                    this.save(btn, afterSaveUrl);
                },
                onPrevious: function () {
                    let btn = $('#btnPrevious');
                    let afterSaveUrl = '/my-listings/' + this.listing.id + '/add-listing-step-2';
                    this.save(btn, afterSaveUrl);
                },
                save: function (btn, afterSaveUrl) {
                    let vm = this;
                    let components = [vm.$refs.listingLocationComponent];
                    if (MyListingService.componentsValid(components)) {
                        QlUtil.UI.btnStartLoading(btn);
                        MyListingService.updateListingPartial({path: 'LOCATION', value: this.listing})
                            .then(function (response) {
                                QlUtil.UI.btnStopLoading(btn);
                                // move the user to the next page
                                window.location.href = afterSaveUrl;
                            })
                            .catch(function (error) {
                                QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                                QlUtil.UI.btnStopLoading(btn);
                            });
                    } else {
                        QlUtil.UI.Notification.showError({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        });
                    }
                }
            }
        });
    }
};