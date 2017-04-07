(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlConfigurationDialogController', QlConfigurationDialogController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i<total; i++)
                    input.push(i);
                return input;
            };
        });

    QlConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QlConfiguration', 'AlertService'];

    function QlConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QlConfiguration, AlertService) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.qlConfiguration = entity;
        vm.clear = clear;
        vm.save = save;




        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.qlConfiguration.id !== null) {
                QlConfiguration.update(vm.qlConfiguration, onSaveSuccess, onSaveError);
            } else {
                QlConfiguration.save(vm.qlConfiguration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:qlConfigurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
