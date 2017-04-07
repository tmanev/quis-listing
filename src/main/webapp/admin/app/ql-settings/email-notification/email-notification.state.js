(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('email-notifications', {
            parent: 'ql-settings',
            url: '/email-notifications?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.emailNotification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/ql-settings/email-notification/email-notifications.html',
                    controller: 'EmailNotificationsController',
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
                    $translatePartialLoader.addPart('email-notification');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })




    .state('email-notifications.delete', {
                parent: 'email-notifications',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/ql-settings/email-notification/email-notification-delete-dialog.html',
                        controller: 'EmailNotificationDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['EmailNotification', function(EmailNotification) {
                                return EmailNotification.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('email-notifications', null, { reload: 'email-notifications' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })


             .state('email-notification-detail', {
                        parent: 'email-notifications',
                        url: '/email-notifications/{id}',
                        data: {
                            authorities: ['ROLE_USER'],
                            pageTitle: 'quisListingApp.emailNotification.detail.title'
                        },
                        views: {
                            'content@': {
                                templateUrl: 'admin/app/ql-settings/email-notification/email-notification-detail.html',
                                controller: 'EmailNotificationDetailController',
                                controllerAs: 'vm'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('email-notification');
                                return $translate.refresh();
                            }],
                            entity: ['$stateParams', 'EmailNotification', function($stateParams, EmailNotification) {
                                return EmailNotification.get({id : $stateParams.id}).$promise;
                            }],
                            previousState: ["$state", function ($state) {
                                var currentStateData = {
                                    name: $state.current.name || 'email-notifications',
                                    params: $state.params,
                                    url: $state.href($state.current.name, $state.params)
                                };
                                return currentStateData;
                            }]
                        }
                    })

                     .state('email-notifications.edit', {
                                parent: 'email-notifications',
                                url: '/{id}/edit',
                                data: {
                                    authorities: ['ROLE_USER']
                                },
                                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                                    $uibModal.open({
                                        templateUrl: 'admin/app/ql-settings/email-notification/email-notification-dialog.html',
                                        controller: 'EmailNotificationDialogController',
                                        controllerAs: 'vm',
                                        backdrop: 'static',
                                        size: 'lg',
                                        resolve: {
                                            entity: ['EmailNotification', function(EmailNotification) {
                                                return EmailNotification.get({id : $stateParams.id}).$promise;
                                            }]
                                        }
                                    }).result.then(function() {
                                        $state.go('email-notifications', null, { reload: 'email-notifications' });
                                    }, function() {
                                        $state.go('^');
                                    });
                                }]
                            })

                            .state('email-notification-detail.edit', {
                                        parent: 'email-notification-detail',
                                        url: '/detail/edit',
                                        data: {
                                            authorities: ['ROLE_USER']
                                        },
                                        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                                            $uibModal.open({
                                                templateUrl: 'admin/app/ql-settings/email-notification/email-notification-dialog.html',
                                                controller: 'EmailNotificationDialogController',
                                                controllerAs: 'vm',
                                                backdrop: 'static',
                                                size: 'lg',
                                                resolve: {
                                                    entity: ['EmailNotification', function(EmailNotification) {
                                                        return EmailNotification.get({id : $stateParams.id}).$promise;
                                                    }]
                                                }
                                            }).result.then(function() {
                                                $state.go('^', {}, { reload: false });
                                            }, function() {
                                                $state.go('^');
                                            });
                                        }]
                                    })

                                    .state('email-notifications.new', {
                                                parent: 'email-notifications',
                                                url: '/new',
                                                data: {
                                                    authorities: ['ROLE_USER']
                                                },
                                                params: {selectedLanguageCode: null},
                                                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                                                    $uibModal.open({
                                                        templateUrl: 'admin/app/ql-settings/email-notification/email-notification-dialog.html',
                                                        controller: 'EmailNotificationDialogController',
                                                        controllerAs: 'vm',
                                                        backdrop: 'static',
                                                        size: 'lg',
                                                        resolve: {
                                                            entity: function () {
                                                                return {
                                                                    id: null,
                                                                    name: null,
                                                                    text: null

                                                                };
                                                            }
                                                        }
                                                    }).result.then(function() {
                                                        $state.go('email-notifications', null, { reload: 'email-notifications' });
                                                    }, function() {
                                                        $state.go('email-notifications');
                                                    });
                                                }]
                                            })



            ;
            }

})();
