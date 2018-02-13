Listing = {
    init: function (senderName, senderEmail, jsTranslations) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;

        var listingApp = new Vue({
            el: '#listingApp',
            data: {
                dlmessage: {
                    id: '',
                    senderId: '',
                    senderName: senderName,
                    senderEmail: senderEmail,
                    createdDate: '',
                    receiverId: '',
                    text: '',
                    listingId: ''
                }
            },
            validations: {
                dlmessage: {
                    senderName: {
                        required: required
                    },
                    senderEmail: {
                        required: required
                    },
                    text: {
                        required: required
                    }
                }
            },
            methods : {
                showContactForm: function () {
                    document.getElementById("nameLabel").style.display = "block";
                    document.getElementById("senderName").style.display = "block";
                    document.getElementById("emailLabel").style.display = "block";
                    document.getElementById("senderEmail").style.display = "block";
                    document.getElementById("textLabel").style.display = "block";
                    document.getElementById("text").style.display = "block";
                    document.getElementById("btnSend").style.display = "block";
                },
                onSubmit: function (event) {
                    if (this.$v.dlmessage.$invalid) {
                        this.$v.dlmessage.$touch();
                    } else {
                        var $btn = $('#btnNext').button('loading');
                        this.dlmessage.listingId = document.getElementById("listingId").value;
                        var payload = this.dlmessage;
                        this.$http({
                            url: '/api/dl-messages',
                            body: payload,
                            method: 'POST'
                        }).then(function (response) {
                            $.notify({
                                message: jsTranslations['page.my_listings.edit_listing.label.sendmessage.success']
                            }, {
                                type: 'success'
                            });
                            $btn.button('reset');
                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: jsTranslations['page.my_listings.edit_listing.label.sendmessage.failure']
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
