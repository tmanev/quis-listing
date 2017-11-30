(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldGroupDialogController', DlContentFieldGroupDialogController);

    DlContentFieldGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DlContentFieldGroup'];

    function DlContentFieldGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DlContentFieldGroup) {
        var vm = this;

        vm.dlContentFieldGroup = entity;
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
            if (vm.dlContentFieldGroup.id !== null) {
                DlContentFieldGroup.update(vm.dlContentFieldGroup, onSaveSuccess, onSaveError);
            } else {
                DlContentFieldGroup.save(vm.dlContentFieldGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:dlContentFieldGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
