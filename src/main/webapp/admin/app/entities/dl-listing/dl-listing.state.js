(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dl-listings', {
            parent: 'entity',
            url: '/dl-listings?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dl-listing.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-listing/dl-listings.html',
                    controller: 'DlListingsController',
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
                    $translatePartialLoader.addPart('dl-listing');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('dl-listing-detail', {
            parent: 'dl-listings',
            url: '/dl-listings/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlListing.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-listing/dl-listing-detail.html',
                    controller: 'DlListingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dl-listing');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DlListing', function($stateParams, DlListing) {
                    return DlListing.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dl-listings',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dl-listing-detail.edit', {
            parent: 'dl-listing-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-listing/dl-listing-dialog.html',
                    controller: 'DlListingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlListing', function(DlListing) {
                            return DlListing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dl-listings.new', {
            parent: 'dl-listings',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            params: {selectedLanguageCode: null},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-listing/dl-listing-dialog.html',
                    controller: 'DlListingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                publicationDate: null,
                                price: null,
                                id: null,
                                languageCode: $stateParams.selectedLanguageCode
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dl-listings', null, { reload: 'dl-listings' });
                }, function() {
                    $state.go('dl-listings');
                });
            }]
        })
        .state('dl-listings.edit', {
            parent: 'dl-listings',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-listing/dl-listing-dialog.html',
                    controller: 'DlListingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlListing', function(DlListing) {
                            return DlListing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-listings', null, { reload: 'dl-listings' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dl-listings.delete', {
            parent: 'dl-listings',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-listing/dl-listing-delete-dialog.html',
                    controller: 'DlListingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DlListing', function(DlListing) {
                            return DlListing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-listings', null, { reload: 'dl-listings' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
