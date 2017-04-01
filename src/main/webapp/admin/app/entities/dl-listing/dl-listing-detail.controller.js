(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlListingDetailController', DlListingDetailController);

    DlListingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DlListing'];

    function DlListingDetailController($scope, $rootScope, $stateParams, previousState, entity, DlListing) {
        var vm = this;

        vm.dlListing = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:dlListingUpdate', function(event, result) {
            vm.dlListing = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
