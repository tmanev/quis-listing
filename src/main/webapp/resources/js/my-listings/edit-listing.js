EditListing = {
    init: function (dlListingDTO, dlContentFieldsDto, jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        const touchMap = new WeakMap();
        ListingDetailsComponent.setContentFieldValuesFromDlListing(dlListingDTO, dlContentFieldsDto);

        let editListingApp = new Vue({
            el: '#editListingApp',
            data: {
                dlContentFields: dlContentFieldsDto,
                listing: listingDtoToListingForm(dlListingDTO),
                saveModal: {
                    callback: null
                },
                editParts: [
                    {
                        uid: 'description',
                        open: false,
                        btnSaveLoading: false
                    },
                    {
                        uid: 'details',
                        open: false,
                        btnSaveLoading: false
                    },
                    {
                        uid: 'location',
                        open: false,
                        btnSaveLoading: false
                    },
                    {
                        uid: 'gallery',
                        open: false
                    }
                ],
                btnPublishLoading: false,
                btnConfirmListingDeleteLoading: false
            },
            validations: function () {
                return {};
            },
            methods: {
                confirmDeleteListing: function () {
                    this.$refs.confirmDeleteListingModal.show();
                },
                cancelDeleteListing: function () {
                    this.$refs.confirmDeleteListingModal.hide();
                },
                deleteListing: function () {
                    let vm = this;
                    vm.btnConfirmListingDeleteLoading = true;
                    MyListingService.deleteListing(this.listing.id).then(success).catch(error);
                    function success() {
                        vm.btnConfirmListingDeleteLoading = false;
                        window.location.href = "/my-listings";
                    }

                    function error (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        vm.btnConfirmListingDeleteLoading = false;
                    }
                },
                onSaveAsDraft: function () {
                    let vm = this;
                    let btn = $('.btn-save-as-draft');
                    this.listing.status = 'DRAFT';
                    QlUtil.UI.btnStartLoading(btn);
                    MyListingService.updateListingPartial({path: 'STATUS', value: this.listing}).then(success).catch(error);
                    function success(response) {
                        QlUtil.UI.btnStopLoading(btn);
                        $.notify({
                            message: jsTranslations['rest.general.save_success']
                        }, {
                            type: 'success'
                        });
                    }
                    function error(error) {
                        $.notify({
                            message: error.data
                        }, {
                            type: 'danger'
                        });
                        QlUtil.UI.btnStopLoading(btn);
                    }
                },
                onPublish: function (event) {
                    let vm = this;

                    let components = [vm.$refs.listingDescriptionComponent, vm.$refs.listingCategoryComponent, vm.$refs.listingDetailsComponent, vm.$refs.listingLocationComponent];
                    if (MyListingService.componentsValid(components)) {
                        vm.btnPublishLoading = true;
                        vm.listing.status = 'PUBLISHED';
                        MyListingService.updateListingPartial({path: 'STATUS', value: vm.listing}).then(success).catch(error);

                        function success() {
                            vm.btnPublishLoading = false;
                            window.location.href = "/my-listings/" + vm.listing.id + "/publish-successful";
                        }

                        function error (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            vm.btnPublishLoading = false;
                        }
                    } else {
                        $.notify({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        }, {
                            type: 'danger'
                        });
                    }
                },
                onSettings: function () {

                },
                onSaveDescription: function () {
                    let vm = this;

                    let components = [vm.$refs.listingDescriptionComponent, vm.$refs.listingCategoryComponent];
                    if (MyListingService.componentsValid(components)) {
                        vm.editParts[0].btnSaveLoading = true;
                        MyListingService.updateListingPartial({path: 'DESCRIPTION', value: this.listing}).then(success).catch(error);
                    } else {
                        $.notify({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        }, {
                            type: 'danger'
                        });
                    }
                    function success(response) {
                        vm.editParts[0].btnSaveLoading = false;
                        $.notify({
                            message: jsTranslations['rest.general.save_success']
                        }, {
                            type: 'success'
                        });
                        vm.editParts[0].open = !vm.editParts[0].open;
                    }
                    function error(error) {
                        $.notify({
                            message: error.data
                        }, {
                            type: 'danger'
                        });
                        vm.editParts[0].btnSaveLoading = false;
                    }
                },
                onSaveDetails: function () {
                    let vm = this;
                    let components = [vm.$refs.listingDetailsComponent];
                    if (MyListingService.componentsValid(components)) {
                        let vm = this;
                        let listingDetailsComponent = vm.$refs.listingDetailsComponent;
                        vm.listing.dlListingFields = listingDetailsComponent.getListingFields();
                        vm.editParts[1].btnSaveLoading = true;
                        MyListingService.updateListingPartial({path: 'DETAILS', value: this.listing})
                            .then(function (response) {
                                vm.editParts[1].btnSaveLoading = false;
                                $.notify({
                                    message: jsTranslations['rest.general.save_success']
                                }, {
                                    type: 'success'
                                });
                                vm.editParts[1].open = !vm.editParts[1].open;
                            })
                            .catch(function (error) {
                                $.notify({
                                    message: error.data
                                }, {
                                    type: 'danger'
                                });
                                vm.editParts[1].btnSaveLoading = false;
                            });
                    } else {
                        $.notify({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        }, {
                            type: 'danger'
                        });
                    }
                },
                onSaveLocation: function () {
                    let vm = this;
                    let components = [vm.$refs.listingLocationComponent];
                    if (MyListingService.componentsValid(components)) {
                        vm.editParts[2].btnSaveLoading = true;
                        MyListingService.updateListingPartial({path: 'LOCATION', value: vm.listing})
                            .then(function (response) {
                                vm.editParts[2].btnSaveLoading = false;
                                $.notify({
                                    message: jsTranslations['rest.general.save_success']
                                }, {
                                    type: 'success'
                                });
                                vm.editParts[2].open = !vm.editParts[2].open;
                            })
                            .catch(function (error) {
                                $.notify({
                                    message: error.data
                                }, {
                                    type: 'danger'
                                });
                                vm.editParts[2].btnSaveLoading = false;
                            });
                    } else {
                        $.notify({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        }, {
                            type: 'danger'
                        });
                    }
                },
                openEdit(editPart) {
                  editPart.open = !editPart.open;
                },
                getPayload: function () {
                    let listingDetailsComponent = MyListingService.getComponentByName('listing-details-component', this.$children);
                    this.listing.dlListingFields = listingDetailsComponent.getListingFields();
                    return this.listing;
                },
                delayTouch: function ($v) {
                    $v.$reset();
                    if (touchMap.has($v)) {
                        clearTimeout(touchMap.get($v));
                    }
                    touchMap.set($v, setTimeout($v.$touch, 1000));
                },
                callbackSave: function () {
                    this.saveModal.callback();
                },
                onSave: function (event) {
                    if (this.listing.status == 'PUBLISHED') {
                        this.saveModal.callback = function () {
                            let $btn = $('#btnSave').button('loading');
                            $('#save-warning-modal').modal('hide');
                            editListingApp.doSave($btn, false);
                        };
                        // open confirmation dialog
                        $('#save-warning-modal').modal('show');
                    } else {
                        let $btn = $('#btnSave').button('loading');
                        this.doSave($btn, false);
                    }
                },
                doSave: function ($btn, locationAfterSave) {
                    let payload = this.getPayload();

                    this.$http({url: '/api/dl-listings', body: payload, method: 'PUT'}).then(function (response) {
                        console.log('Success!:', response.data);
                        this.listing.status = response.data.status;
                        let successMsg = $('#msg_save_success').text();
                        $.notify({
                            message: successMsg
                        }, {
                            type: 'success'
                        });
                        $btn.button('reset');
                        if (locationAfterSave) {
                            window.location.href = locationAfterSave;
                        }
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        $btn.button('reset');
                    });
                },
                onGoBack: function (event) {
                    window.location.href = "/my-listings";
                }
            }
        });
    }
};