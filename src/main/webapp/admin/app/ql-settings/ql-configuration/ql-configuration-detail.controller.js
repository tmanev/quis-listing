(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlConfigurationDetailController', QlConfigurationDetailController);

    QlConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'QlConfiguration'];

    function QlConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, QlConfiguration) {
        var vm = this;

        vm.qlConfiguration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:qlConfigurationUpdate', function(event, result) {
            vm.qlConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
