(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlLocationDetailController', DlLocationDetailController);

    DlLocationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DlLocation'];

    function DlLocationDetailController($scope, $rootScope, $stateParams, previousState, entity, DlLocation) {
        var vm = this;

        vm.dlLocation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:dlLocationUpdate', function (event, result) {
            vm.dlLocation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
