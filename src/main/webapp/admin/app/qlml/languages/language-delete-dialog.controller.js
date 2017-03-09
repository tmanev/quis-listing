(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('LanguageDeleteController',LanguageDeleteController);

    LanguageDeleteController.$inject = ['$uibModalInstance', 'entity', 'Language'];

    function LanguageDeleteController($uibModalInstance, entity, Language) {
        var vm = this;

        vm.language = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Language.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
