EditListing = {
    init: function (dlListingDTO, dlCategoriesDtoFlat, dlContentFieldsDto, dlLocationCountries, dlLocationStates, dlLocationCities, jsTranslations, commonVar) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        /**
         * @return {boolean}
         */
        const TwoWordValidator = function (value, component) {
            var s = value;
            s = s.replace(/(^\s*)|(\s*$)/gi, "");
            s = s.replace(/[ ]{2,}/gi, " ");
            s = s.replace(/\n /, "\n");
            return s.split(' ').length >= 2;
        };

        /**
         * @return {boolean}
         */
        const ListHasSelectionValidator = function (selectedValue, component) {
            return selectedValue != -1;
        };

        const touchMap = new WeakMap();

        var roots = MyListingsComponent.Utils.flatItemsToTree(dlCategoriesDtoFlat);

        for (var i = 0; i < dlContentFieldsDto.length; i++) {
            var dlContentField = dlContentFieldsDto[i];

            if (dlContentField.options) {
                dlContentField.optionsModel = JSON.parse(dlContentField.options);
            }

            getValueFromContentField(dlContentField, dlListingDTO.dlListingFields);
        }

        function getValueFromContentField(dlContentField, dlListingFields) {

            if (dlListingFields) {
                for (let dlListingField of dlListingFields) {
                    if (dlListingField.id == dlContentField.id) {
                        if (dlContentField.type === 'CHECKBOX') {
                            if (dlListingField.selectedValue) {
                                dlContentField.selectedValue = JSON.parse(dlListingField.selectedValue);
                            } else {
                                dlContentField.selectedValue = [];
                            }
                            return;
                        } else if (dlContentField.type === 'DEPENDENT_SELECT') {
                            if (dlListingField.selectedValue) {
                                dlContentField.parentValue = findDlContentFieldItem(dlListingField.selectedValue, dlContentField.dlContentFieldItems);
                                dlContentField.selectedValue = dlListingField.selectedValue;
                            } else {
                                dlContentField.parentValue = -1;
                                dlContentField.selectedValue = -1;
                            }
                            return;
                        } else if (dlContentField.type === 'SELECT') {
                            if (dlListingField.selectedValue) {
                                dlContentField.selectedValue = dlListingField.selectedValue;
                            } else {
                                dlContentField.selectedValue = -1;
                            }
                            return;
                        } else if (dlContentField.type === 'NUMBER_UNIT') {
                            if (dlListingField.selectedValue) {
                                dlContentField.selectedValue = dlListingField.selectedValue;
                            } else {
                                dlContentField.selectedValue = -1;
                            }
                            dlContentField.value = dlListingField.value;
                            return;
                        } else {
                            dlContentField.value = dlListingField.value;
                            return;
                        }
                    }
                }
                // if it gets here means content field is added and there is no relation with the dlListing
                // return default value for the proper type
                return defaultValueIfNoRelation(dlContentField);
            } else {
                return defaultValueIfNoRelation(dlContentField);
            }

            function findDlContentFieldItem(id, dlContentFieldItems) {
                for (let dlContentFieldItem of dlContentFieldItems) {
                    if (dlContentFieldItem.id == id) {
                        return dlContentFieldItem.parent.id;
                    }
                }
            }

            function defaultValueIfNoRelation(dlContentField) {
                if (dlContentField.type === 'CHECKBOX') {
                    // return empty array to be able to operate checkbox
                    dlContentField.selectedValue = [];
                } else if (dlContentField.type === 'SELECT') {
                    dlContentField.selectedValue = -1;
                } else if (dlContentField.type === 'DEPENDENT_SELECT') {
                    dlContentField.parentValue = -1;
                    dlContentField.selectedValue = -1;
                } else if (dlContentField.type === 'NUMBER_UNIT') {
                    dlContentField.selectedValue = -1;
                }
            }
        }

        var selectedCategory = {
            name: ''
        };
        if (dlListingDTO.dlCategories && dlListingDTO.dlCategories.length > 0) {
            selectedCategory = dlListingDTO.dlCategories[0];
        }

        var selectedCityId = -1;
        var selectedStateId = -1;
        var selectedCountryId = -1;
        if (dlListingDTO.dlLocations && dlListingDTO.dlLocations.length > 0) {
            selectedCityId = dlListingDTO.dlLocations[0].id;
            selectedStateId = dlListingDTO.dlLocations[0].parentId;
            selectedCountryId = dlListingDTO.dlLocations[0].parent.parent.id;
        }

        var editListingApp = new Vue({
            el: '#editListingApp',
            data: {
                queuePos: 0,
                queueFiles: [],
                categories: roots,
                dlContentFields: dlContentFieldsDto,
                selectedCountry: selectedCountryId,
                isStateSelectLoading: false,
                isCitySelectLoading: false,
                selectedState: selectedStateId,
                selectedCity: selectedCityId,
                selectedCategory: selectedCategory,
                dlLocationCountries: dlLocationCountries,
                dlLocationStates: dlLocationStates,
                dlLocationCities: dlLocationCities,
                listing: {
                    id: dlListingDTO.id,
                    title: dlListingDTO.title,
                    content: dlListingDTO.content,
                    name: dlListingDTO.name,
                    translatedName: dlListingDTO.translatedName,
                    status: dlListingDTO.status,
                    dlCategories: dlListingDTO.dlCategories,
                    dlLocations: dlListingDTO.dlLocations,
                    dlListingFields: dlListingDTO.dlListingFields,
                    attachments: dlListingDTO.attachments,
                    featuredAttachment: dlListingDTO.featuredAttachment
                },
                image: '',
                confirmRemoveImageModal: {
                    attachment: null
                },
                saveModal: {
                    shouldGoBack: false
                }
            },
            validations: function () {
                let validation_dict = {
                    listing: {
                        title: {
                            required: required,
                            TwoWordValidator: TwoWordValidator
                        }
                    },
                    selectedCity: {
                        ListHasSelectionValidator: ListHasSelectionValidator
                    }
                };

                validation_dict.dlContentFields = {};

                for (let index = 0; index < this.dlContentFields.length; index++) {
                    validation_dict.dlContentFields[index] = {};
                    validation_dict.dlContentFields[index].value = {};
                    validation_dict.dlContentFields[index].selectedValue = {};
                    if (this.dlContentFields[index].required) {
                        if (this.dlContentFields[index].type === 'SELECT' || this.dlContentFields[index].type === 'DEPENDENT_SELECT') {
                            validation_dict.dlContentFields[index].selectedValue.ListHasSelectionValidator = ListHasSelectionValidator;
                        } else if(this.dlContentFields[index].type === 'NUMBER_UNIT') {
                            validation_dict.dlContentFields[index].selectedValue.ListHasSelectionValidator = ListHasSelectionValidator;
                            validation_dict.dlContentFields[index].value.required = required;
                        } else {
                            validation_dict.dlContentFields[index].value.required = required;
                        }
                    }

                    if (this.dlContentFields[index].type === 'NUMBER' || this.dlContentFields[index].type === 'NUMBER_UNIT') {
                        let min = Number.MIN_SAFE_INTEGER;
                        let max = Number.MAX_SAFE_INTEGER;

                        if (this.dlContentFields[index].optionsModel.min && this.dlContentFields[index].optionsModel.min !== '') {
                            min = this.dlContentFields[index].optionsModel.min;
                        }

                        if (this.dlContentFields[index].optionsModel.max && this.dlContentFields[index].optionsModel.max !== '') {
                            max = this.dlContentFields[index].optionsModel.max;
                        }

                        validation_dict.dlContentFields[index].value.between = between(min, max);
                    }

                    if (this.dlContentFields[index].type === 'STRING') {
                        if (this.dlContentFields[index].optionsModel.minLength && this.dlContentFields[index].optionsModel.minLength !== '') {
                            validation_dict.dlContentFields[index].value.minLength = minLength(this.dlContentFields[index].optionsModel.minLength);
                        } else {
                            validation_dict.dlContentFields[index].value.minLength = minLength(0);
                        }

                        if (this.dlContentFields[index].optionsModel.maxLength && this.dlContentFields[index].optionsModel.maxLength !== '') {
                            validation_dict.dlContentFields[index].value.maxLength = maxLength(this.dlContentFields[index].optionsModel.maxLength);
                        } else {
                            validation_dict.dlContentFields[index].value.maxLength = maxLength(4096);
                        }
                    }
                }

                return validation_dict;
            },
            methods: {
                getPayload: function () {
                    let dlListingFields = [];
                    for (let dlContentField of this.dlContentFields) {
                        let value;
                        let selectedValue;
                        if (dlContentField.type === 'CHECKBOX') {
                            if (dlContentField.selectedValue) {
                                selectedValue = JSON.stringify(dlContentField.selectedValue);
                            } else {
                                selectedValue = JSON.stringify([]);
                            }
                        } else if (dlContentField.type === 'SELECT' || dlContentField.type === 'DEPENDENT_SELECT') {
                            selectedValue = dlContentField.selectedValue;
                        } else if (dlContentField.type === 'NUMBER_UNIT') {
                            selectedValue = dlContentField.selectedValue;
                            value = dlContentField.value;
                        } else {
                            value = dlContentField.value;
                        }
                        let listingField = {
                            id: dlContentField.id,
                            value: value,
                            selectedValue: selectedValue
                        };
                        dlListingFields.push(listingField);
                    }

                    this.listing.dlListingFields = dlListingFields;
                    if (this.selectedCity !== "-1") {
                        this.listing.dlLocations = [{
                            id: this.selectedCity
                        }];
                    } else {
                        this.listing.dlLocations = [];
                    }

                    this.listing.dlCategories = [this.selectedCategory];

                    return this.listing;
                },
                fileUpload: function (files) {
                    console.log(files);

                    var index = this.queueFiles.push({
                        file: files[0],
                        progress: 0
                    });
                    var local = this.queueFiles;
                    var listing = this.listing;
                    var fd = new FormData();
                    fd.append(files[0].fileName, files[0]);
                    // Ajax Submit
                    $.ajax({
                        url: '/api/dl-listings/' + this.listing.id + '/upload',
                        type: 'POST',
                        dataType: 'json',
                        data: fd,
                        cache: false,
                        contentType: false,
                        processData: false,
                        forceSync: false,
                        xhr: function () {
                            var xhrobj = $.ajaxSettings.xhr();
                            if (xhrobj.upload) {
                                xhrobj.upload.addEventListener('progress', function (event) {
                                    var percent = 0;
                                    var position = event.loaded || event.position;
                                    var total = event.total || event.totalSize;
                                    if (event.lengthComputable) {
                                        percent = Math.ceil(position / total * 100);
                                    }

                                    // widget.settings.onUploadProgress.call(widget.element, widget.queuePos, percent);
                                    console.log(percent);
                                    local[index - 1].progress = percent;
                                }, false);
                            }

                            return xhrobj;
                        },
                        success: function (data, message, xhr) {
                            // widget.settings.onUploadSuccess.call(widget.element, widget.queuePos, data);
                            console.log("Success upload");
                            listing.attachments = data.attachments;
                            $.notify({
                                message: 'Success upload'
                            }, {
                                type: 'success'
                            });

                        },
                        error: function (xhr, status, errMsg) {
                            // widget.settings.onUploadError.call(widget.element, widget.queuePos, errMsg);
                            console.log("Error upload");
                        },
                        complete: function (xhr, textStatus) {
                            // widget.processQueue();
                            console.log("Complete upload");
                        }
                    });

                },
                onDrop: function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                    var files = e.dataTransfer.files;
                    this.fileUpload(files);
                },
                onChange: function (e) {
                    var files = e.target.files;
                    this.fileUpload(files);
                },
                createFile: function (file) {
                    if (!file.type.match('image.*')) {
                        alert('Select an image');
                        return;
                    }
                    var img = new Image();
                    var reader = new FileReader();
                    var vm = this;

                    reader.onload = function (e) {
                        vm.image = e.target.result;
                    };
                    reader.readAsDataURL(file);
                },
                confirmRemoveImage: function (attachment) {
                    this.confirmRemoveImageModal.attachment = attachment;
                    $('#my-modal').modal('show');
                },
                removeImage: function (attachment) {
                    this.$http({
                        url: '/api/dl-listings/' + this.listing.id + '/attachments/' + attachment.id,
                        method: 'DELETE'
                    }).then(function (response) {
                        console.log('Success!:', response.data);

                        let index = this.listing.attachments.indexOf(attachment);
                        this.listing.attachments.splice(index, 1);
                        $.notify({
                            message: response.headers.get('X-qlService-alert')
                        }, {
                            type: 'success'
                        });
                        this.confirmRemoveImageModal.attachment = null;
                        $('#my-modal').modal('hide');
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.confirmRemoveImageModal.attachment = null;
                        $('#my-modal').modal('hide');
                    });
                },
                onBoxClick: function (e) {
                    $(e.target.firstElementChild).trigger('click');
                },
                rootContentFieldItem: function(dlContentFieldItems) {
                    return dlContentFieldItems.filter(function (dlContentFieldItem) {
                        return dlContentFieldItem.parent === null;
                    });
                },
                childContentFieldItem: function(dlContentFieldItems, parentId) {
                    return dlContentFieldItems.filter(function (dlContentFieldItem) {
                        return dlContentFieldItem.parent !== null && dlContentFieldItem.parent.id == parentId;
                    });
                },
                resetValue: function(dlContentField) {
                    dlContentField.value = -1;
                },
                onCountryChange: function () {
                    if (this.selectedCountry === "-1") {
                        this.dlLocationStates = [];
                        this.selectedState = -1;
                        this.dlLocationCities = [];
                        this.selectedCity = -1;
                    } else {
                        var params = {
                            parentId: this.selectedCountry
                        };
                        this.isStateSelectLoading = true;
                        this.selectedState = -1;
                        this.selectedCity = -1;
                        this.$http({url: '/api/dl-locations', params: params, method: 'GET'}).then(function (response) {
                            console.log('Success!:', response.data);
                            this.dlLocationStates = response.data;
                            this.isStateSelectLoading = false;
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            this.isStateSelectLoading = false;
                        });
                    }

                },
                onStateChange: function () {
                    if (this.selectedState === "-1") {
                        this.dlLocationCities = [];
                        this.selectedCity = -1;
                    } else {
                        var params = {
                            parentId: this.selectedState
                        };
                        this.isCitySelectLoading = true;
                        this.selectedCity = -1;
                        this.$http({url: '/api/dl-locations', params: params, method: 'GET'}).then(function (response) {
                            console.log('Success!:', response.data);
                            this.dlLocationCities = response.data;
                            this.isCitySelectLoading = false;
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            this.isCitySelectLoading = false;
                        });
                    }
                },
                delayTouch: function ($v) {
                    $v.$reset();
                    if (touchMap.has($v)) {
                        clearTimeout(touchMap.get($v));
                    }
                    touchMap.set($v, setTimeout($v.$touch, 1000));
                },
                callbackSave: function () {
                    if (this.saveModal.shouldGoBack) {
                        var $btn = $('#btnSaveAndGoBack').button('loading');
                        $('#save-warning-modal').modal('hide');
                        this.doSave($btn, "/my-listings");
                    } else {
                        var $btn = $('#btnSave').button('loading');
                        $('#save-warning-modal').modal('hide');
                        this.doSave($btn, false);
                    }
                },
                onSaveAndGoBack: function (event) {
                    if (this.listing.status == 'PUBLISHED') {
                        // open confirmation dialog
                        this.saveModal.shouldGoBack = "/my-listings";
                        $('#save-warning-modal').modal('show');
                    } else {
                        var $btn = $('#btnSaveAndGoBack').button('loading');
                        this.doSave($btn, "/my-listings");
                    }
                },
                onSave: function (event) {
                    if (this.listing.status == 'PUBLISHED') {
                        // open confirmation dialog
                        this.saveModal.shouldGoBack = false;
                        $('#save-warning-modal').modal('show');
                    } else {
                        var $btn = $('#btnSave').button('loading');
                        this.doSave($btn, false);
                    }
                },
                doSave: function ($btn, locationAfterSave) {
                    var payload = this.getPayload();

                    this.$http({url: '/api/dl-listings', body: payload, method: 'PUT'}).then(function (response) {
                        console.log('Success!:', response.data);
                        var successMsg = $('#msg_save_success').text();
                        this.listing.status = response.data.status;
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
                onPublish: function (event) {
                    if (this.$v.$invalid) {
                        this.$v.$touch();
                        $.notify({
                            title: "<strong>" + jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.title'] + "</strong>",
                            message: jsTranslations['page.my_listings.edit_listing.notifications.publish_validation.message']
                        }, {
                            type: 'danger',
                            timer: 0
                        });
                    } else {
                        let payload = this.getPayload();
                        var $btn = $('#btnPublish').button('loading');
                        this.$http({
                            url: '/api/dl-listings/publish',
                            body: payload,
                            method: 'PUT'
                        }).then(function (response) {
                            console.log('Success!:', response.data);

                            // $.notify({
                            //     message: response.headers.get('X-qlService-alert')
                            // }, {
                            //     type: 'success'
                            // });
                            $btn.button('reset');
                            window.location.href = "/my-listings/" + this.listing.id + "/publish-successful";
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
                },
                onGoBack: function (event) {
                    window.location.href = "/my-listings";
                },
                openCategorySelection: function ($v) {
                    // $v.$touch();
                    // this.delayTouch($v);
                    $('#myModal').modal('toggle');
                }
            }, computed: {
                thumbnails: function (attachments) {
                    return attachments.filter(function (attachment) {
                        for (let imageResizeMeta of attachment.attachmentMetadata.imageResizeMetas) {
                            if (imageResizeMeta.name === 'dl-thumbnail') {
                                return true;
                            }
                        }

                    });
                }
            }
        });

        commonVar.addListingApp = editListingApp;

        editListingApp.$on('id-selected', function (category) {
            if (this.selectedCategory) {
                this.selectedCategory.active = false;
            }
            this.selectedCategory = category;
            this.selectedCategory.active = true;
        });
    }
};