MyMessages = {
    init: function (jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;

        var messagesApp = new Vue({
            el: '#messagesApp',
            data: {
                isLoading: false,
                dlMessages: [],
                confirmModal: {
                    messageToDelete: null
                }
            },
            validations: {

            },
            methods : {
                fetchMyMessages: function () {
                    this.isLoading = true;
                    this.$http({
                        url: '/api/dl-messages',
                        headers: {
                            'Authorization': 'Bearer ' + Cookies.get('ql-auth').split(":")[1]
                        },
                        method: 'GET'
                    }).then(function (response) {
                        for (var i = 0; i<response.data.length; i++) {
                            this.dlMessages.push(response.data[i]);
                        }
                        this.isLoading = false;
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.isLoading = false;
                    });
                },
                confirmDeleteMessages: function(dlMessage) {
                    this.confirmModal.messageToDelete = dlMessage;
                    $('#my-modal').modal('show');
                },
                deleteMessages: function () {
                    this.$http({
                        url: '/api/dl-messages/' + this.confirmModal.messageToDelete.listingId,
                        method: 'DELETE'
                    }).then(function (response) {
                        let index = this.dlMessages.indexOf(this.confirmModal.messageToDelete);
                        this.dlMessages.splice(index, 1);
                        $.notify({
                            message: jsTranslations['page.my_messages.notifications.delete_listing_success']
                        }, {
                            type: 'success'
                        });
                        this.confirmModal.messageToDelete = null;
                        $('#my-modal').modal('hide');
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.confirmModal.messageToDelete = null;
                        $('#my-modal').modal('hide');
                    });
                }
            },
            created: function () {
                this.fetchMyMessages();
            }
        });
    }
};
