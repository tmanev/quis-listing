(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dl-categories', {
            parent: 'entity',
            url: '/dl-categories?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlCategory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-category/dl-categories.html',
                    controller: 'DlCategoriesController',
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

        .state('dl-category-detail', {
            parent: 'dl-categories',
            url: '/dl-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.dlCategory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/entities/dl-category/dl-category-detail.html',
                    controller: 'DlCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dl-category');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DlCategory', function($stateParams, DlCategory) {
                    return DlCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dl-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('dl-category-detail.edit', {
            parent: 'dl-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
                    controller: 'DlCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlCategory', function(DlCategory) {
                            return DlCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('dl-categories.new', {
            parent: 'dl-categories',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            params: {selectedLanguageCode: null},
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
                    controller: 'DlCategoryDialogController',
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
                                languageCode: $stateParams.selectedLanguageCode,
                                sourceLanguageCode: null,
                                translationGroupId: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dl-categories', null, { reload: 'dl-categories' });
                }, function() {
                    $state.go('dl-categories');
                });
            }]
        })

            .state('dl-categories.add-translation', {
                parent: 'dl-categories',
                url: '/add-translation',
                data: {
                    authorities: ['ROLE_USER']
                },
                params: {id: null, selectedLanguageCode: null, sourceLanguageCode: null, translationGroupId: null},
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
                        controller: 'DlCategoryDialogController',
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
                                    languageCode: $stateParams.selectedLanguageCode,
                                    sourceLanguageCode: $stateParams.sourceLanguageCode,
                                    translationGroupId: $stateParams.translationGroupId
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('dl-categories', null, { reload: 'dl-categories' });
                    }, function() {
                        $state.go('dl-categories');
                    });
                }]
            })

            .state('dl-categories.edit-translation', {
                parent: 'dl-categories',
                url: '/by-translation/{id}/edit-translation',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
                        controller: 'DlCategoryDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['DlCategory', function(DlCategory) {
                                return DlCategory.getByTranslation({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('dl-categories', null, { reload: 'dl-categories' });
                    }, function() {
                        $state.go('dl-categories');
                    });
                }]
            })

        .state('dl-categories.edit', {
            parent: 'dl-categories',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-category/dl-category-dialog.html',
                    controller: 'DlCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DlCategory', function(DlCategory) {
                            return DlCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-categories', null, { reload: 'dl-categories' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('dl-categories.delete', {
            parent: 'dl-categories',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/entities/dl-category/dl-category-delete-dialog.html',
                    controller: 'DlCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DlCategory', function(DlCategory) {
                            return DlCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dl-categories', null, { reload: 'dl-categories' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
