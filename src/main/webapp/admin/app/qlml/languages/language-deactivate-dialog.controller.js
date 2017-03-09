(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('LanguageDeactivateController',LanguageDeactivateController);

    LanguageDeactivateController.$inject = ['$scope', '$uibModalInstance', 'entity', 'Language'];

    function LanguageDeactivateController($scope, $uibModalInstance, entity, Language) {
        var vm = this;

        vm.language = entity;
        vm.clear = clear;
        vm.confirmDeactivate = confirmDeactivate;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDeactivate () {
            vm.isSaving = true;
            if (vm.language.id !== null) {
                vm.language.active = false;
                Language.update(vm.language, onSaveSuccess, onSaveError);
            }

            function onSaveSuccess (result) {
                $scope.$emit('quisListingApp:languageUpdate', result);
                $uibModalInstance.close(result);
                vm.isSaving = false;
            }

            function onSaveError () {
                vm.isSaving = false;
            }
        }
    }
})();
