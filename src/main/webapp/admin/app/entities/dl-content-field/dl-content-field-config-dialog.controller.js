(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldConfigDialogController', DlContentFieldConfigDialogController)
    ;

    DlContentFieldConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DlContentField', 'AlertService'];

    function DlContentFieldConfigDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, DlContentField, AlertService) {
        var vm = this;
        vm.predicate = 'id';

        vm.dlContentField = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.dlContentField.id !== null) {
                DlContentField.update(vm.dlContentField, onSaveSuccess, onSaveError);
            } else {
                DlContentField.save(vm.dlContentField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quisListingApp:dlContentFieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
