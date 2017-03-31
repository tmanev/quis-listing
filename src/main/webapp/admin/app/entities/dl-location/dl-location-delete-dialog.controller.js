(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlLocationDeleteController', DlLocationDeleteController);

    DlLocationDeleteController.$inject = ['$uibModalInstance', 'entity', 'DlLocation'];

    function DlLocationDeleteController($uibModalInstance, entity, DlLocation) {
        var vm = this;

        vm.dlLocation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            DlLocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
