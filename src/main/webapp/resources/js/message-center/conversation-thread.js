let ConversationThread = {
    init: function (dlMessageOverview, dlMessages, jsTranslations, totalDlMessages, loadedDlMessages) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, between, email} = window.validators;

        let conversationThreadApp = new Vue({
            el: '#conversationThreadApp',
            filters: {
                fullTime: function(date) {
                    return moment(date).format('MMMM Do YYYY, h:mm');
                }
            },
            data: {
                pagingParams: {
                    page: 1,
                    sort: null,
                    search: null,
                    itemsPerPage: 6,
                    totalDlMessages: totalDlMessages,
                    loadedDlMessages: loadedDlMessages,
                    url: '/api/dl-messages/' + dlMessageOverview.id,
                    isLoading: false
                },
                dlMessageOverview: dlMessageOverview,
                dlMessages: dlMessages,
                dlMessage: {
                    lastMessageId: null,
                    text: ''
                },
                isLoading: false,
                btnSendMessageLoading: false,
                btnLoadMoreLoading: false
            },
            validations: {
                dlMessage: {
                    text: {
                        required: required
                    }
                }
            },
            methods: {
                onSendMessage: function () {
                    let vm = this;
                    if (vm.$v.dlMessage.$invalid) {
                        vm.$v.dlMessage.$touch();
                    } else {
                        vm.btnSendMessageLoading = true;
                        let payload = vm.dlMessage;
                        axios.post('/api/dl-messages/' + vm.dlMessageOverview.id, payload, {
                            headers: {
                                'Authorization': QlUtil.Rest.authorizationBearer()
                            }
                        }).then(function (response) {
                            QlUtil.UI.Notification.showSuccess({message: jsTranslations['page.message_center.conversation_thread.label.sendmessage.success']});
                            vm.dlMessage.text = '';
                            vm.$v.dlMessage.$reset();
                            vm.btnSendMessageLoading = false;
                            vm.dlMessages.unshift(response.data);
                        }, function () {
                            QlUtil.UI.Notification.showError({message: jsTranslations['page.message_center.conversation_thread.label.sendmessage.failure']});
                            vm.btnSendMessageLoading = false;
                        });
                    }
                },
                onLoadNextDlMessages: function () {
                    let vm = this;
                    vm.pagingParams.isLoading = true;
                    vm.btnLoadMoreLoading = true;
                    axios.get(vm.pagingParams.url, {
                        params: {
                            page: vm.pagingParams.page,
                            size: vm.pagingParams.itemsPerPage,
                            languageCode: Cookies.get('ql-lang-key')
                        },
                        headers: {
                            'Authorization': QlUtil.Rest.authorizationBearer()
                        }
                    }).then(function (response) {
                        for (let i = 0; i<response.data.length; i++) {
                            vm.dlMessages.push(response.data[i]);
                        }
                        vm.pagingParams.page++;
                        vm.pagingParams.loadedDlMessages += response.data.length;
                        vm.pagingParams.isLoading = false;
                        vm.btnLoadMoreLoading = false;
                    }).catch(function (response) {
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        vm.pagingParams.isLoading = false;
                        vm.btnLoadMoreLoading = false;
                    });
                }
            }
        });
    }
};
