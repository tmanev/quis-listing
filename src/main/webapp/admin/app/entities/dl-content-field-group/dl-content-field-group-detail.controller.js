(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldGroupDetailController', DlContentFieldGroupDetailController);

    DlContentFieldGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DlContentFieldGroup'];

    function DlContentFieldGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, DlContentFieldGroup) {
        var vm = this;

        vm.dlContentFieldGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:dlContentFieldGroupUpdate', function(event, result) {
            vm.dlContentFieldGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
