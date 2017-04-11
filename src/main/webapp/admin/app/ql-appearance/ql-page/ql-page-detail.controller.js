(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlPageDetailController', QlPageDetailController);

    QlPageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QlPage'];

    function QlPageDetailController($scope, $rootScope, $stateParams, previousState, entity, QlPage) {
        var vm = this;

        vm.qlPage = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:qlPageUpdate', function(event, result) {
            vm.qlPage = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
