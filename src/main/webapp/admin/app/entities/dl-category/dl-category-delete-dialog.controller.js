(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlCategoryDeleteController',DlCategoryDeleteController);

    DlCategoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'DlCategory'];

    function DlCategoryDeleteController($uibModalInstance, entity, DlCategory) {
        var vm = this;

        vm.dlCategory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DlCategory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
