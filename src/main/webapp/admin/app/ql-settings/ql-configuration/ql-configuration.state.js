(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ql-configurations', {
            parent: 'ql-settings',
            url: '/ql-configurations?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.qlConfiguration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/ql-settings/ql-configuration/ql-configurations.html',
                    controller: 'QlConfigurationsController',
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
                    $translatePartialLoader.addPart('ql-configuration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        .state('ql-configurations.new', {
                    parent: 'ql-configurations',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    params: {selectedLanguageCode: null},
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-settings/ql-configuration/ql-configuration-dialog.html',
                            controller: 'QlConfigurationDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: function () {
                                    return {
                                        id: null,
                                        key: null,
                                        value: null

                                    };
                                }
                            }
                        }).result.then(function() {
                            $state.go('ql-configurations', null, { reload: 'ql-configurations' });
                        }, function() {
                            $state.go('ql-configurations');
                        });
                    }]
                })

        .state('ql-configurations.edit', {
                    parent: 'ql-configurations',
                    url: '/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-settings/ql-configuration/ql-configuration-dialog.html',
                            controller: 'QlConfigurationDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: ['QlConfiguration', function(QlConfiguration) {
                                    return QlConfiguration.get({id : $stateParams.id}).$promise;
                                }]
                            }
                        }).result.then(function() {
                            $state.go('ql-configurations', null, { reload: 'ql-configurations' });
                        }, function() {
                            $state.go('^');
                        });
                    }]
                })

        .state('ql-configuration-detail.edit', {
                    parent: 'ql-configuration-detail',
                    url: '/detail/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-settings/ql-configuration/ql-configuration-dialog.html',
                            controller: 'QlConfigurationDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: ['QlConfiguration', function(QlConfiguration) {
                                    return QlConfiguration.get({id : $stateParams.id}).$promise;
                                }]
                            }
                        }).result.then(function() {
                            $state.go('^', {}, { reload: false });
                        }, function() {
                            $state.go('^');
                        });
                    }]
                })

        .state('ql-configuration-detail', {
                    parent: 'ql-configurations',
                    url: '/ql-configurations/{id}',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'quisListingApp.qlConfiguration.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'admin/app/ql-settings/ql-configuration/ql-configuration-detail.html',
                            controller: 'QlConfigurationDetailController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('ql-configuration');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'QlConfiguration', function($stateParams, QlConfiguration) {
                            return QlConfiguration.get({id : $stateParams.id}).$promise;
                        }],
                        previousState: ["$state", function ($state) {
                            var currentStateData = {
                                name: $state.current.name || 'ql-configurations',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }
                })

        .state('ql-configurations.delete', {
                    parent: 'ql-configurations',
                    url: '/{id}/delete',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-settings/ql-configuration/ql-configuration-delete-dialog.html',
                            controller: 'QlConfigurationDeleteController',
                            controllerAs: 'vm',
                            size: 'md',
                            resolve: {
                                entity: ['QlConfiguration', function(QlConfiguration) {
                                    return QlConfiguration.get({id : $stateParams.id}).$promise;
                                }]
                            }
                        }).result.then(function() {
                            $state.go('ql-configurations', null, { reload: 'ql-configurations' });
                        }, function() {
                            $state.go('^');
                        });
                    }]
                })

        ;
    }

})();
