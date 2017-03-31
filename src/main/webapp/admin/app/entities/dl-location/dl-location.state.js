(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dl-locations', {
            parent: 'entity',
            url: '/dl-locations?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlLocation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-location/dl-locations.html',
                    controller: 'DlLocationsController',
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
                    $translatePartialLoader.addPart('dl-location');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        .state('dl-location-detail', {
            parent: 'dl-locations',
            url: '/dl-location/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlLocation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-location/dl-location-detail.html',
                    controller: 'DlLocationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dl-location');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DlLocation', function($stateParams, DlLocation) {
                    return DlLocation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dl-location',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('dl-location-detail.edit', {
            parent: 'dl-location-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-location/dl-location-dialog.html',
                    controller: 'DlLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlLocation', function(DlLocation) {
                            return DlLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('dl-locations.new', {
            parent: 'dl-locations',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            params: {selectedLanguageCode: null},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-location/dl-location-dialog.html',
                    controller: 'DlLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null,
                                name: null,
                                slug: null,
                                description: "",
                                parentId: null,
                                count: null,
                                languageCode: $stateParams.selectedLanguageCode
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dl-locations', null, { reload: 'dl-locations' });
                }, function() {
                    $state.go('dl-locations');
                });
            }]
        })

        .state('dl-locations.edit', {
            parent: 'dl-locations',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-location/dl-location-dialog.html',
                    controller: 'DlLocationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlLocation', function(DlLocation) {
                            return DlLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-locations', null, { reload: 'dl-locations' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('dl-locations.delete', {
            parent: 'dl-locations',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-location/dl-location-delete-dialog.html',
                    controller: 'DlLocationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DlLocation', function(DlLocation) {
                            return DlLocation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-locations', null, { reload: 'dl-locations' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
