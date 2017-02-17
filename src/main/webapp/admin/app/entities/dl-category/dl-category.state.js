(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dl-category', {
            parent: 'entity',
            url: '/dl-category?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-category/dl-categories.html',
                    controller: 'DlCategoryController',
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
                    $translatePartialLoader.addPart('dl-category');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        // .state('dl-category-detail', {
        //     parent: 'dl-category',
        //     url: '/dl-category/{id}',
        //     data: {
        //         authorities: ['ROLE_USER'],
        //         pageTitle: 'quisListingApp.dlCategory.detail.title'
        //     },
        //     views: {
        //         'content@': {
        //             templateUrl: 'admin/app/entities/dl-category/dl-category-detail.html',
        //             controller: 'DlCategoryDetailController',
        //             controllerAs: 'vm'
        //         }
        //     },
        //     resolve: {
        //         translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
        //             $translatePartialLoader.addPart('dlCategory');
        //             return $translate.refresh();
        //         }],
        //         entity: ['$stateParams', 'DlCategory', function($stateParams, DlCategory) {
        //             return DlCategory.get({id : $stateParams.id}).$promise;
        //         }],
        //         previousState: ["$state", function ($state) {
        //             var currentStateData = {
        //                 name: $state.current.name || 'dl-category',
        //                 params: $state.params,
        //                 url: $state.href($state.current.name, $state.params)
        //             };
        //             return currentStateData;
        //         }]
        //     }
        // })

        // .state('dl-category-detail.edit', {
        //     parent: 'dl-category-detail',
        //     url: '/detail/edit',
        //     data: {
        //         authorities: ['ROLE_USER']
        //     },
        //     onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
        //             controller: 'DlCategoryDialogController',
        //             controllerAs: 'vm',
        //             backdrop: 'static',
        //             size: 'lg',
        //             resolve: {
        //                 entity: ['DlCategory', function(DlCategory) {
        //                     return DlCategory.get({id : $stateParams.id}).$promise;
        //                 }]
        //             }
        //         }).result.then(function() {
        //             $state.go('^', {}, { reload: false });
        //         }, function() {
        //             $state.go('^');
        //         });
        //     }]
        // })

        // .state('dl-category.new', {
        //     parent: 'dl-category',
        //     url: '/new',
        //     data: {
        //         authorities: ['ROLE_USER']
        //     },
        //     onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
        //             controller: 'DlCategoryDialogController',
        //             controllerAs: 'vm',
        //             backdrop: 'static',
        //             size: 'lg',
        //             resolve: {
        //                 entity: function () {
        //                     return {
        //                         name: null,
        //                         slug: null,
        //                         count: null,
        //                         id: null
        //                     };
        //                 }
        //             }
        //         }).result.then(function() {
        //             $state.go('dl-category', null, { reload: 'dl-category' });
        //         }, function() {
        //             $state.go('dl-category');
        //         });
        //     }]
        // })

        // .state('dl-category.edit', {
        //     parent: 'dl-category',
        //     url: '/{id}/edit',
        //     data: {
        //         authorities: ['ROLE_USER']
        //     },
        //     onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
        //             controller: 'DlCategoryDialogController',
        //             controllerAs: 'vm',
        //             backdrop: 'static',
        //             size: 'lg',
        //             resolve: {
        //                 entity: ['DlCategory', function(DlCategory) {
        //                     return DlCategory.get({id : $stateParams.id}).$promise;
        //                 }]
        //             }
        //         }).result.then(function() {
        //             $state.go('dl-category', null, { reload: 'dl-category' });
        //         }, function() {
        //             $state.go('^');
        //         });
        //     }]
        // })

        // .state('dl-category.delete', {
        //     parent: 'dl-category',
        //     url: '/{id}/delete',
        //     data: {
        //         authorities: ['ROLE_USER']
        //     },
        //     onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
        //         $uibModal.open({
        //             templateUrl: 'admin/app/entities/dl-category/dl-category-delete-dialog.html',
        //             controller: 'DlCategoryDeleteController',
        //             controllerAs: 'vm',
        //             size: 'md',
        //             resolve: {
        //                 entity: ['DlCategory', function(DlCategory) {
        //                     return DlCategory.get({id : $stateParams.id}).$promise;
        //                 }]
        //             }
        //         }).result.then(function() {
        //             $state.go('dl-category', null, { reload: 'dl-category' });
        //         }, function() {
        //             $state.go('^');
        //         });
        //     }]
        // })
        ;
    }

})();
