(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ql-strings', {
            parent: 'qlml',
            url: '/ql-strings?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.ql-string.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/qlml/ql-string/ql-strings.html',
                    controller: 'QlStringController',
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
                    $translatePartialLoader.addPart('ql-string');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })






        ;
    }

})();