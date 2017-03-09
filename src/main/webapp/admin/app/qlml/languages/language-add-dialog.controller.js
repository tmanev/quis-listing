(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('LanguageAddController',LanguageAddController);

    LanguageAddController.$inject = ['$scope', '$uibModalInstance', 'entity', 'Language'];

    function LanguageAddController($scope, $uibModalInstance, entity, Language) {
        var vm = this;

        vm.language = entity;

        vm.clear = clear;
        vm.add = add;

        loadAll();

        function loadAll() {
            Language.query({
                active: false
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.languages = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function add () {
            vm.isSaving = true;
            if (vm.language.id !== null) {
                vm.language.active = true;
                Language.update(vm.language, onSaveSuccess, onSaveError);
            }
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
})();
