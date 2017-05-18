(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('email-templates', {
            parent: 'ql-settings',
            url: '/email-templates?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'quisListingApp.emailTemplate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'admin/app/ql-settings/email-template/email-templates.html',
                    controller: 'EmailTemplatesController',
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
                    $translatePartialLoader.addPart('email-template');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })




    .state('email-templates.delete', {
                parent: 'email-templates',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'admin/app/ql-settings/email-template/email-template-delete-dialog.html',
                        controller: 'EmailTemplateDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['EmailTemplate', function(EmailTemplate) {
                                return EmailTemplate.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('email-templates', null, { reload: 'email-templates' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })


             .state('email-template-detail', {
                        parent: 'email-templates',
                        url: '/email-templates/{id}',
                        data: {
                            authorities: ['ROLE_USER'],
                            pageTitle: 'quisListingApp.emailTemplate.detail.title'
                        },
                        views: {
                            'content@': {
                                templateUrl: 'admin/app/ql-settings/email-template/email-template-detail.html',
                                controller: 'EmailTemplateDetailController',
                                controllerAs: 'vm'
                            }
                        },
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('email-template');
                                return $translate.refresh();
                            }],
                            entity: ['$stateParams', 'EmailTemplate', function($stateParams, EmailTemplate) {
                                return EmailTemplate.get({id : $stateParams.id}).$promise;
                            }],
                            previousState: ["$state", function ($state) {
                                var currentStateData = {
                                    name: $state.current.name || 'email-templates',
                                    params: $state.params,
                                    url: $state.href($state.current.name, $state.params)
                                };
                                return currentStateData;
                            }]
                        }
                    })

                     .state('email-templates.edit', {
                                parent: 'email-templates',
                                url: '/{id}/edit',
                                data: {
                                    authorities: ['ROLE_USER']
                                },
                                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                                    $uibModal.open({
                                        templateUrl: 'admin/app/ql-settings/email-template/email-template-dialog.html',
                                        controller: 'EmailTemplateDialogController',
                                        controllerAs: 'vm',
                                        backdrop: 'static',
                                        size: 'lg',
                                        resolve: {
                                            entity: ['EmailTemplate', function(EmailTemplate) {
                                                return EmailTemplate.get({id : $stateParams.id}).$promise;
                                            }]
                                        }
                                    }).result.then(function() {
                                        $state.go('email-templates', null, { reload: 'email-templates' });
                                    }, function() {
                                        $state.go('^');
                                    });
                                }]
                            })

                            .state('email-template-detail.edit', {
                                        parent: 'email-template-detail',
                                        url: '/detail/edit',
                                        data: {
                                            authorities: ['ROLE_USER']
                                        },
                                        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                                            $uibModal.open({
                                                templateUrl: 'admin/app/ql-settings/email-template/email-template-dialog.html',
                                                controller: 'EmailTemplateDialogController',
                                                controllerAs: 'vm',
                                                backdrop: 'static',
                                                size: 'lg',
                                                resolve: {
                                                    entity: ['EmailTemplate', function(EmailTemplate) {
                                                        return EmailTemplate.get({id : $stateParams.id}).$promise;
                                                    }]
                                                }
                                            }).result.then(function() {
                                                $state.go('^', {}, { reload: false });
                                            }, function() {
                                                $state.go('^');
                                            });
                                        }]
                                    })

                                    .state('email-templates.new', {
                                                parent: 'email-templates',
                                                url: '/new',
                                                data: {
                                                    authorities: ['ROLE_USER']
                                                },
                                                params: {selectedLanguageCode: null},
                                                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                                                    $uibModal.open({
                                                        templateUrl: 'admin/app/ql-settings/email-template/email-template-dialog.html',
                                                        controller: 'EmailTemplateDialogController',
                                                        controllerAs: 'vm',
                                                        backdrop: 'static',
                                                        size: 'lg',
                                                        resolve: {
                                                            entity: function () {
                                                                return {
                                                                    id: null,
                                                                    name: null,
                                                                    text: null
                                                                };
                                                            }
                                                        }
                                                    }).result.then(function() {
                                                        $state.go('email-templates', null, { reload: 'email-templates' });
                                                    }, function() {
                                                        $state.go('email-templates');
                                                    });
                                                }]
                                            })
            ;
            }

})();
