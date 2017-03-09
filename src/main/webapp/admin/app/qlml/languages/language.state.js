(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('languages', {
            parent: 'qlml',
            url: '/languages?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.language.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/qlml/languages/languages.html',
                    controller: 'LanguagesController',
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
                    $translatePartialLoader.addPart('language');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

        .state('language-detail', {
            parent: 'languages',
            url: '/language/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.language.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/qlml/languages/language-detail.html',
                    controller: 'LanguageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('language');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Language', function($stateParams, Language) {
                    return Language.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'languages',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

        .state('language-detail.edit', {
            parent: 'language-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/qlml/languages/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('languages.new', {
            parent: 'languages',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/qlml/languages/language-dialog.html',
                    controller: 'LanguageDialogController',
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
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('languages');
                });
            }]
        })

        .state('languages.add', {
            parent: 'languages',
            url: '/add',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/qlml/languages/language-add-dialog.html',
                    controller: 'LanguageAddController',
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
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('languages');
                });
            }]
        })

        .state('languages.edit', {
            parent: 'languages',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/qlml/languages/language-dialog.html',
                    controller: 'LanguageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('languages.delete', {
            parent: 'languages',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'admin/app/qlml/languages/language-delete-dialog.html',
                    controller: 'LanguageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Language', function(Language) {
                            return Language.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('languages', null, { reload: 'languages' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
    }

})();
