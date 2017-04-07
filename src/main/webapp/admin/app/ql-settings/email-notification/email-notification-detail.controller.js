(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailNotificationDetailController', EmailNotificationDetailController);

    EmailNotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EmailNotification'];

    function EmailNotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, EmailNotification) {
        var vm = this;

        vm.emailNotification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:emailNotificationUpdate', function(event, result) {
            vm.emailNotification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
