(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('NavMenuDeleteController',NavMenuDeleteController);

    NavMenuDeleteController.$inject = ['$uibModalInstance', 'entity', 'NavMenu'];

    function NavMenuDeleteController($uibModalInstance, entity, NavMenu) {
        var vm = this;

        vm.navMenu = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            NavMenu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
