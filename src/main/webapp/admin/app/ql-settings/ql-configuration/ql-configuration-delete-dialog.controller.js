(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlConfigurationDeleteController',QlConfigurationDeleteController);

    QlConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'QlConfiguration'];

    function QlConfigurationDeleteController($uibModalInstance, entity, QlConfiguration) {
        var vm = this;

        vm.qlConfiguration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            QlConfiguration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
