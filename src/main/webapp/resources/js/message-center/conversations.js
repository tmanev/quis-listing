let Conversations = {
    init: function (jsTranslations, tableFields) {
        Vue.use(window.vuelidate.default);

        let vm = new Vue({
            el: '#conversationsApp',
            filters: {
                truncate: function (value) {
                    if (!value.length > 60) {
                        return value.substring(0, 60) + '...'
                    }
                    return value;
                },
                fullTime: function(date) {
                    return moment(date).format('MMMM Do YYYY, h:mm');
                }
            },
            data: {
                isLoading: false,
                dlMessages: [],
                fields: tableFields,
                confirmModal: {
                    conversationToDelete: null
                },
                pagingParams: {
                    currentPage: 1,
                    perPage: 5,
                    pageOptions: [ 5, 10, 15 ],
                    sort: null,
                    url: '/api/dl-messages',
                    totalRows: 0
                },
                btnConfirmConversationDeleteLoading: false
            },
            validations: {},
            methods: {
                dlMessagesProvider: function (ctx) {
                    let vm = this;
                    let params = {
                            page: ctx.currentPage - 1,
                            size: ctx.perPage,
                            sort: ctx.sortBy
                    };
                    let promise = axios.get(vm.pagingParams.url, {
                        params: params,
                        headers: {
                            'Authorization': QlUtil.Rest.authorizationBearer()
                        }
                    });

                    return promise.then((response) => {
                        const items = response.data;
                        // Here we could override the busy state, setting isBusy to false
                        // this.isBusy = false
                        vm.pagingParams.totalRows = response.headers['x-total-count'];
                        return(items)
                    }).catch(error => {
                        // Here we could override the busy state, setting isBusy to false
                        // this.isBusy = false
                        // Returning an empty array, allows table to correctly handle busy state in case of error
                        return [];
                    })
                },
                fetchMyMessages: function () {
                    let vm = this;
                    vm.isLoading = true;
                    axios.get('/api/dl-messages', {
                        headers: {
                            'Authorization': QlUtil.Rest.authorizationBearer()
                        }
                    }).then(function (response) {
                        for (let i = 0; i < response.data.length; i++) {
                            vm.dlMessages.push(response.data[i]);
                        }
                        vm.isLoading = false;
                    }).catch(function (response) {
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        vm.isLoading = false;
                    });
                },
                confirmDeleteConversation: function (dlMessage) {
                    vm.confirmModal.conversationToDelete = dlMessage;
                    vm.$refs.confirmDeleteConversationModal.show();
                },
                cancelDeleteConversation: function () {
                    vm.$refs.confirmDeleteConversationModal.hide();
                },
                deleteConversation: function () {
                    vm.btnConfirmConversationDeleteLoading = true;
                    axios.delete('/api/dl-messages/' + vm.confirmModal.conversationToDelete.id, {
                        headers: {
                            'Authorization': QlUtil.Rest.authorizationBearer()
                        }
                    }).then(function () {
                        QlUtil.UI.Notification.showSuccess({message: jsTranslations['page.message_center.conversations.notifications.delete_listing_success']});
                        vm.$refs.confirmDeleteConversationModal.hide();
                        vm.$refs.conversationsTable.refresh();
                        vm.confirmModal.conversationToDelete = null;
                        vm.btnConfirmConversationDeleteLoading = false;
                    }).catch(function () {
                        QlUtil.UI.Notification.showError({message: jsTranslations['info.general_server_error']});
                        vm.confirmModal.conversationToDelete = null;
                        vm.btnConfirmConversationDeleteLoading = false;
                        vm.$refs.confirmDeleteConversationModal.hide();
                    });
                },
                fullTime: function(date) {
                    return moment(date).format('MMMM Do YYYY, h:mm');
                }
            },
            created: function () {
                // this.fetchMyMessages();
            }
        });
    }
};
