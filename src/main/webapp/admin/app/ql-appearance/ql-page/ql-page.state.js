(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ql-pages', {
            parent: 'ql-appearance',
            url: '/ql-pages?page&sort&search',
            data: {
                            authorities: ['ROLE_USER'],
                            pageTitle: 'quisListingApp.ql-page.home.title'
                        },
            views: {
                            'content@': {
                                templateUrl: 'admin/app/ql-appearance/ql-page/ql-pages.html',
                                controller: 'QlPagesController',
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
                                $translatePartialLoader.addPart('ql-page');
                                $translatePartialLoader.addPart('global');
                                return $translate.refresh();
                            }]
                        }

        })
        .state('ql-page-detail', {
                    parent: 'ql-pages',
                    url: '/ql-pages/{id}',
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'quisListingApp.qlPage.detail.title'
                    },
                    views: {
                        'content@': {
                            templateUrl: 'admin/app/ql-appearance/ql-page/ql-page-detail.html',
                            controller: 'QlPageDetailController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('ql-page');
                            return $translate.refresh();
                        }],
                        entity: ['$stateParams', 'QlPage', function($stateParams, QlPage) {
                            return QlPage.get({id : $stateParams.id}).$promise;
                        }],
                        previousState: ["$state", function ($state) {
                            var currentStateData = {
                                name: $state.current.name || 'ql-page',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }
                })
        .state('ql-page-detail.edit', {
                    parent: 'ql-page-detail',
                    url: '/detail/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-appearance/ql-page/ql-page-dialog.html',
                            controller: 'QlPageDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: ['QlPage', function(QlPage) {
                                    return QlPage.get({id : $stateParams.id}).$promise;
                                }]
                            }
                        }).result.then(function() {
                            $state.go('^', {}, { reload: false });
                        }, function() {
                            $state.go('^');
                        });
                    }]
                })
        .state('ql-pages.new', {
                    parent: 'ql-pages',
                    url: '/new',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    params: {selectedLanguageCode: null},
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-appearance/ql-page/ql-page-dialog.html',
                            controller: 'QlPageDialogController',
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
                            $state.go('ql-pages', null, { reload: 'ql-pages' });
                        }, function() {
                            $state.go('ql-pages');
                        });
                    }]
                })
        .state('ql-pages.edit', {
                    parent: 'ql-pages',
                    url: '/{id}/edit',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-appearance/ql-page/ql-page-dialog.html',
                            controller: 'QlPageDialogController',
                            controllerAs: 'vm',
                            backdrop: 'static',
                            size: 'lg',
                            resolve: {
                                entity: ['QlPage', function(QlPage) {
                                    return QlPage.get({id : $stateParams.id}).$promise;
                                }]
                            }
                        }).result.then(function() {
                            $state.go('ql-pages', null, { reload: 'ql-pages' });
                        }, function() {
                            $state.go('^');
                        });
                    }]
                })
        .state('ql-pages.delete', {
                    parent: 'ql-pages',
                    url: '/{id}/delete',
                    data: {
                        authorities: ['ROLE_USER']
                    },
                    onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                        $uibModal.open({
                            templateUrl: 'admin/app/ql-appearance/ql-page/ql-page-delete-dialog.html',
                            controller: 'QlPageDeleteController',
                            controllerAs: 'vm',
                            size: 'md',
                            resolve: {
                                entity: ['QlPage', function(QlPage) {
                                    return QlPage.get({id : $stateParams.id}).$promise;
                                }]
                            }
                        }).result.then(function() {
                            $state.go('ql-pages', null, { reload: 'ql-pages' });
                        }, function() {
                            $state.go('^');
                        });
                    }]
                })
        ;
    }
})();
