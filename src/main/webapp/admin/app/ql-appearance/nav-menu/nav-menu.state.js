(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('nav-menus', {
            parent: 'entity',
            url: '/nav-menus?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.navMenu.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/ql-appearance/nav-menu/nav-menus.html',
                    controller: 'NavMenusController',
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
                    $translatePartialLoader.addPart('nav-menu');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        .state('nav-menu-detail', {
            parent: 'nav-menus',
            url: '/nav-menus/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.navMenu.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/ql-appearance/nav-menu/nav-menu-detail.html',
                    controller: 'NavMenuDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('nav-menu');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NavMenu', function($stateParams, NavMenu) {
                    return NavMenu.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'nav-menus',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('nav-menu-detail.edit', {
            parent: 'nav-menu-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/ql-appearance/nav-menu/nav-menu-dialog.html',
                    controller: 'NavMenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NavMenu', function(NavMenu) {
                            return NavMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('nav-menus.new', {
            parent: 'nav-menus',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            params: {selectedLanguageCode: null},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/ql-appearance/nav-menu/nav-menu-dialog.html',
                    controller: 'NavMenuDialogController',
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
                    $state.go('nav-menus', null, { reload: 'nav-menus' });
                }, function() {
                    $state.go('nav-menus');
                });
            }]
        })

        .state('nav-menus.edit', {
            parent: 'nav-menus',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/ql-appearance/nav-menu/nav-menu-dialog.html',
                    controller: 'NavMenuDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NavMenu', function(NavMenu) {
                            return NavMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('nav-menus', null, { reload: 'nav-menus' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('nav-menus.delete', {
            parent: 'nav-menus',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/ql-appearance/nav-menu/nav-menu-delete-dialog.html',
                    controller: 'NavMenuDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NavMenu', function(NavMenu) {
                            return NavMenu.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('nav-menus', null, { reload: 'nav-menus' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
