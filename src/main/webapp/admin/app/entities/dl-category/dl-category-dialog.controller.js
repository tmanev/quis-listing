(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlCategoryDialogController', DlCategoryDialogController);

    DlCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DlCategory'];

    function DlCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DlCategory) {
        var vm = this;

        vm.dlCategory = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dlCategory.id !== null) {
                DlCategory.update(vm.dlCategory, onSaveSuccess, onSaveError);
            } else {
                DlCategory.save(vm.dlCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:dlCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
