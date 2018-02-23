var ListingAttachmentComponent = {
    init: function (jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        Vue.component('listing-attachment-component', {
            template: '#listing-attachment-component',
            props: {
                listing: Object
            },
            data: function () {
                return {
                    listing: this.listing,
                    queuePos: 0,
                    queueFiles: [],
                    image: '',
                    confirmRemoveImageModal: {
                        attachment: null
                    }
                };
            },
            validations: function () {
                return {};
            },
            methods: {
                runUpload: function (files) {
                    for (let i = 0; i < files.length; i++) {
                        doUpload(this.listing, this.queueFiles, files[i]);
                    }

                    function doUpload(dlListing, queueFiles, file) {
                        let index = queueFiles.push({
                            file: file,
                            progress: 0
                        });
                        let local = queueFiles;
                        let listing = dlListing;
                        let fd = new FormData();
                        fd.append(file.name, file);
                        // Ajax Submit
                        $.ajax({
                            url: '/api/dl-listings/' + listing.id + '/upload',
                            type: 'POST',
                            headers: {
                                'Authorization': QlUtil.Rest.authorizationBearer()
                            },
                            dataType: 'json',
                            data: fd,
                            cache: false,
                            contentType: false,
                            processData: false,
                            forceSync: false,
                            xhr: function () {
                                let xhrobj = $.ajaxSettings.xhr();
                                if (xhrobj.upload) {
                                    xhrobj.upload.addEventListener('progress', function (event) {
                                        let percent = 0;
                                        let position = event.loaded || event.position;
                                        let total = event.total || event.totalSize;
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
                                for (let i = 0; i < data.length; i++) {
                                    listing.attachments.push(data[i]);
                                }

                                QlUtil.UI.Notification.showSuccess({message: jsTranslations['rest.general.upload_success']});
                            },
                            error: function (xhr, status, errMsg) {
                                QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                            },
                            complete: function (xhr, textStatus) {
                                // widget.processQueue();
                                console.log("Complete upload");
                            }
                        });
                    }
                },
                fileUpload: function (files) {
                    this.runUpload(files);
                },
                onDrop: function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                    let files = e.dataTransfer.files;
                    this.fileUpload(files);
                },
                onChange: function (e) {
                    let files = e.target.files;
                    this.fileUpload(files);
                },
                createFile: function (file) {
                    if (!file.type.match('image.*')) {
                        alert('Select an image');
                        return;
                    }
                    let reader = new FileReader();
                    let vm = this;

                    reader.onload = function (e) {
                        vm.image = e.target.result;
                    };
                    reader.readAsDataURL(file);
                },
                confirmRemoveImage: function (attachment) {
                    this.confirmRemoveImageModal.attachment = attachment;
                    $('#confirm-remove-image-modal').modal('show');
                },
                removeImage: function (attachment) {
                    let vm = this;
                    let selectedAttachment = attachment;
                    axios.delete('/api/dl-listings/' + vm.listing.id + '/attachments/' + selectedAttachment.id, {
                        headers: {
                            'Authorization': QlUtil.Rest.authorizationBearer()
                        }
                    }).then(function (response) {
                        console.log('Success!:', response.data);

                        let index = vm.listing.attachments.indexOf(selectedAttachment);
                        vm.listing.attachments.splice(index, 1);
                        QlUtil.UI.Notification.showSuccess({message: jsTranslations['info.save_success']});
                        vm.confirmRemoveImageModal.attachment = null;
                        $('#confirm-remove-image-modal').modal('hide');
                    }).catch(function (response) {
                        console.log('Error!:', response.data);
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        vm.confirmRemoveImageModal.attachment = null;
                        $('#confirm-remove-image-modal').modal('hide');
                    });
                },
                onBoxClick: function (e) {
                    $('#image-upload').trigger('click');
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
            },
            watch: {
                'listing.featuredAttachment': function (val, oldVal) {
                    MyListingService.updateListingPartial({path: 'FEATURED_ATTACHMENT', value: this.listing})
                        .then(function (response) {
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['info.save_success']});
                        })
                        .catch(function (error) {
                            QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        });
                }
            }
        });
    }
};
