AddListingStep1 = {
    init: function (dlListingDTO, jsTranslations) {
        Vue.use(window.vuelidate.default);

        const touchMap = new WeakMap();

        let addListingStep1App = new Vue({
            el: '#addListingStep1App',
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
                    let vm = this;
                    let btn = $('#btnNext');
                    let components = [vm.$refs.listingDescriptionComponent, vm.$refs.listingCategoryComponent];
                    if (MyListingService.componentsValid(components)) {
                        QlUtil.UI.btnStartLoading(btn);
                        if (this.listing.id == null) {
                            MyListingService.createListing(this.listing).then(success).catch(error);
                        } else {
                            MyListingService.updateListingPartial({path: 'DESCRIPTION', value: this.listing}).then(success).catch(error);
                        }
                    } else {
                        QlUtil.UI.Notification.showError({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        });
                    }
                    function success(response) {
                        QlUtil.UI.btnStopLoading(btn);
                        // move the user to the next page
                        window.location.href = '/my-listings/' + response.data.id + '/add-listing-step-2';
                    }
                    function error(error) {
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        QlUtil.UI.btnStopLoading(btn);
                    }
                }
            }
        });
    }
};