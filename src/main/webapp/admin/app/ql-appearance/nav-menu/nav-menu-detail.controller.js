(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('NavMenuDetailController', NavMenuDetailController);

    NavMenuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'NavMenu'];

    function NavMenuDetailController($scope, $rootScope, $stateParams, previousState, entity, NavMenu) {
        var vm = this;

        vm.navMenu = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:navMenuUpdate', function(event, result) {
            vm.navMenu = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
