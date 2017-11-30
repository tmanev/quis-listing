(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldGroupDeleteController',DlContentFieldGroupDeleteController);

    DlContentFieldGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'DlContentFieldGroup'];

    function DlContentFieldGroupDeleteController($uibModalInstance, entity, DlContentFieldGroup) {
        var vm = this;

        vm.dlContentFieldGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DlContentFieldGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
