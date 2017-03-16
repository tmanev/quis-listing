(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('dl-content-fields', {
                parent: 'entity',
                url: '/dl-content-fields?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quisListingApp.dlContentField.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-fields.html',
                        controller: 'DlContentFieldsController',
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
                        $translatePartialLoader.addPart('dl-content-field');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('dl-content-field-detail', {
                parent: 'dl-content-fields',
                url: '/dl-content-fields/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quisListingApp.dlContentField.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-field-detail.html',
                        controller: 'DlContentFieldDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dl-content-field');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DlContentField', function ($stateParams, DlContentField) {
                        return DlContentField.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'dl-content-field',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('dl-content-fields.edit', {
                parent: 'dl-content-fields',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quisListingApp.dlContentField.detail.title'
                },
                params: {selectedLanguageCode: null},
                views: {
                    'content@': {
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-field-detail.html',
                        controller: 'DlContentFieldDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dl-content-field');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DlContentField', function ($stateParams, DlContentField) {
                        return DlContentField.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'dl-content-fields',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('dl-content-fields.new', {
                parent: 'dl-content-fields',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'quisListingApp.dlContentField.detail.title'
                },
                params: {selectedLanguageCode: null},
                views: {
                    'content@': {
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-field-detail.html',
                        controller: 'DlContentFieldDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dl-content-field');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DlContentField', function ($stateParams, DlContentField) {
                        return {
                            id: null,
                            coreField: false,
                            orderNum: 0,
                            name: '',
                            slug: '',
                            description: '',
                            type: '',
                            iconImage: '',
                            required: false,
                            hasConfiguration: false,
                            hasSearchConfiguration: false,
                            canBeOrdered: false,
                            hideName: false,
                            onExcerptPage: true,
                            onListingPage: true,
                            onSearchForm: false,
                            onMap: false,
                            onAdvancedSearchForm: false,
                            categories: null,
                            options: null,
                            searchOptions: null
                        };
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'dl-content-fields',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('dl-content-fields.edit-config', {
                parent: 'dl-content-fields',
                url: '/{id}/edit-config',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-field-config-dialog.html',
                        controller: 'DlContentFieldConfigDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['DlContentField', function (DlContentField) {
                                return DlContentField.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('dl-content-fields', null, {reload: 'dl-content-fields'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('dl-content-fields.edit-search-config', {
                parent: 'dl-content-fields',
                url: '/{id}/edit-search-config',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-field-search-config-dialog.html',
                        controller: 'DlContentFieldSearchConfigDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['DlContentField', function (DlContentField) {
                                return DlContentField.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('dl-content-fields', null, {reload: 'dl-content-fields'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })

            .state('dl-content-fields.delete', {
                parent: 'dl-content-fields',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/entities/dl-content-field/dl-content-field-delete-dialog.html',
                        controller: 'DlContentFieldDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['DlContentField', function (DlContentField) {
                                return DlContentField.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('dl-content-fields', null, {reload: 'dl-content-fields'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
        ;
    }

})();
