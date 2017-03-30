(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('attachments', {
                parent: 'media',
                url: '/attachments?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quisListingApp.attachment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'admin/app/media/attachment/attachments.html',
                        controller: 'AttachmentsController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attachment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('attachment-detail', {
                parent: 'attachments',
                url: '/attachment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quisListingApp.attachment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'admin/app/media/attachment/attachment-detail.html',
                        controller: 'AttachmentDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attachment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Attachment', function ($stateParams, Attachment) {
                        return Attachment.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'attachments',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('attachment-detail.edit', {
                parent: 'attachment-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/media/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Attachment', function (Attachment) {
                                return Attachment.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })

            .state('attachments.new', {
                parent: 'attachments',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/media/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null,
                                    code: null,
                                    englishName: null,
                                    defaultLocale: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('attachments', null, {reload: 'attachments'});
                    }, function () {
                        $state.go('attachments');
                    });
                }]
            })

            .state('attachments.add', {
                parent: 'attachments',
                url: '/add',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/media/attachment/attachment-add-dialog.html',
                        controller: 'AttachmentAddController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null,
                                    code: null,
                                    englishName: null,
                                    defaultLocale: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('attachments', null, {reload: 'attachments'});
                    }, function () {
                        $state.go('attachments');
                    });
                }]
            })

            .state('attachments.edit', {
                parent: 'attachments',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/media/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Attachment', function (Attachment) {
                                return Attachment.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('attachments', null, {reload: 'attachments'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })

            .state('attachments.delete', {
                parent: 'attachments',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/media/attachment/attachment-delete-dialog.html',
                        controller: 'AttachmentDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Attachment', function (Attachment) {
                                return Attachment.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('attachments', null, {reload: 'attachments'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })

        ;
    }

})();
