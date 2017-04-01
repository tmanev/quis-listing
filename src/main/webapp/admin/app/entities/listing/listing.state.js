(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('listing', {
            parent: 'entity',
            url: '/listing?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.listing.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/listing/listings.html',
                    controller: 'ListingController',
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
                    $translatePartialLoader.addPart('listing');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('listing-detail', {
            parent: 'listing',
            url: '/listing/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.listing.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/listing/listing-detail.html',
                    controller: 'ListingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('listing');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Listing', function($stateParams, Listing) {
                    return Listing.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'listing',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('listing-detail.edit', {
            parent: 'listing-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/listing/listing-dialog.html',
                    controller: 'ListingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Listing', function(Listing) {
                            return Listing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('listing.new', {
            parent: 'listing',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            params: {selectedLanguageCode: null},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/listing/listing-dialog.html',
                    controller: 'ListingDialogController',
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
                    $state.go('listing', null, { reload: 'listing' });
                }, function() {
                    $state.go('listing');
                });
            }]
        })
        .state('listing.edit', {
            parent: 'listing',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/listing/listing-dialog.html',
                    controller: 'ListingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Listing', function(Listing) {
                            return Listing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('listing', null, { reload: 'listing' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('listing.delete', {
            parent: 'listing',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/listing/listing-delete-dialog.html',
                    controller: 'ListingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Listing', function(Listing) {
                            return Listing.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('listing', null, { reload: 'listing' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
