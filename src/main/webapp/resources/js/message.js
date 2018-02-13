Message = {
    init: function (dlMessageDTO, jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;

        var messageApp = new Vue({
            el: '#messageApp',
            data: {
                dlmessage: {
                    id: '',
                    senderId: dlMessageDTO.senderId,
                    senderName: dlMessageDTO.senderName,
                    senderEmail: dlMessageDTO.senderEmail,
                    createdDate: '',
                    receiverId: dlMessageDTO.receiverId,
                    text: '',
                    listingId: dlMessageDTO.listingId
                },
                isLoading: false
            },
            validations: {
                dlmessage: {
                    text: {
                        required: required
                    }
                }
            },
            methods: {
                onSubmit: function (event) {
                    if (this.$v.dlmessage.$invalid) {
                        this.$v.dlmessage.$touch();
                    } else {
                        var $btn = $('#btnSend').button('loading');
                        var payload = this.dlmessage;
                        this.$http({
                            url: '/api/dl-messages',
                            body: payload,
                            method: 'POST'
                        }).then(function (response) {
                            $.notify({
                                message: jsTranslations['page.my_message_preview.label.sendmessage.success']
                            }, {
                                type: 'success'
                            });
                            $btn.button('reset');
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: jsTranslations['page.my_message_preview.label.sendmessage.failure']
                            }, {
                                type: 'success'
                            });
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            $btn.button('reset');
                        });
                    }
                }
            }
        });
    }
};
