(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldDeleteController',DlContentFieldDeleteController);

    DlContentFieldDeleteController.$inject = ['$uibModalInstance', 'entity', 'DlContentField'];

    function DlContentFieldDeleteController($uibModalInstance, entity, DlContentField) {
        var vm = this;

        vm.dlContentField = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DlContentField.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
