(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('ListingDetailController', ListingDetailController);

    ListingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Listing'];

    function ListingDetailController($scope, $rootScope, $stateParams, previousState, entity, Listing) {
        var vm = this;

        vm.listing = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:listingUpdate', function(event, result) {
            vm.book = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
