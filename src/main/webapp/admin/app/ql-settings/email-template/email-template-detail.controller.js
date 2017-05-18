(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailTemplateDetailController', EmailTemplateDetailController);

    EmailTemplateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EmailTemplate'];

    function EmailTemplateDetailController($scope, $rootScope, $stateParams, previousState, entity, EmailTemplate) {
        var vm = this;

        vm.emailTemplate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:emailTemplateUpdate', function(event, result) {
            vm.emailTemplate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
