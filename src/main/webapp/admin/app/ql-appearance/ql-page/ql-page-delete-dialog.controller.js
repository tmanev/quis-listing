(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlPageDeleteController',QlPageDeleteController);

    QlPageDeleteController.$inject = ['$uibModalInstance', 'entity', 'QlPage'];

    function QlPageDeleteController($uibModalInstance, entity, QlPage) {
        var vm = this;

        vm.qlPage = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QlPage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
