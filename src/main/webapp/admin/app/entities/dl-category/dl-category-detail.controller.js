(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlCategoryDetailController', DlCategoryDetailController);

    DlCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DlCategory'];

    function DlCategoryDetailController($scope, $rootScope, $stateParams, previousState, entity, DlCategory) {
        var vm = this;

        vm.dlCategory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:dlCategoryUpdate', function(event, result) {
            vm.dlCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
