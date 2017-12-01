(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dl-content-field-groups', {
            parent: 'entity',
            url: '/dl-content-field-groups?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlContentFieldGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-content-field-group/dl-content-field-groups.html',
                    controller: 'DlContentFieldGroupsController',
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
                    $translatePartialLoader.addPart('dl-content-field-group');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        .state('dl-content-field-group-detail', {
            parent: 'dl-content-field-groups',
            url: '/dl-content-field-groups/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlContentFieldGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-content-field-group/dl-content-field-group-detail.html',
                    controller: 'DlContentFieldGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dl-content-field-group');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DlContentFieldGroup', function($stateParams, DlContentFieldGroup) {
                    return DlContentFieldGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dl-content-field-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('dl-content-field-group-detail.edit', {
            parent: 'dl-content-field-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-content-field-group/dl-content-field-group-dialog.html',
                    controller: 'DlContentFieldGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlContentFieldGroup', function(DlContentFieldGroup) {
                            return DlContentFieldGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('dl-content-field-groups.new', {
            parent: 'dl-content-field-groups',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            params: {},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-content-field-group/dl-content-field-group-dialog.html',
                    controller: 'DlContentFieldGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null,
                                name: null,
                                slug: null,
                                description: null,
                                orderNum: 0
                            };
                        }
                    }
                }).result.then(function(result) {
                    $state.go('dl-content-field-groups', {}, { reload: 'dl-content-field-groups' });
                }, function() {
                    $state.go('dl-content-field-groups');
                });
            }]
        })

        .state('dl-content-field-groups.edit', {
            parent: 'dl-content-field-groups',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-content-field-group/dl-content-field-group-dialog.html',
                    controller: 'DlContentFieldGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlContentFieldGroup', function(DlContentFieldGroup) {
                            return DlContentFieldGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-content-field-groups', null, { reload: 'dl-content-field-groups' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('dl-content-field-groups.delete', {
            parent: 'dl-content-field-groups',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-content-field-group/dl-content-field-group-delete-dialog.html',
                    controller: 'DlContentFieldGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DlContentFieldGroup', function(DlContentFieldGroup) {
                            return DlContentFieldGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-content-field-groups', null, { reload: 'dl-content-field-groups' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
