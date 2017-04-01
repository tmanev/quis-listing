(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlListingDeleteController',DlListingDeleteController);

    DlListingDeleteController.$inject = ['$uibModalInstance', 'entity', 'DlListing'];

    function DlListingDeleteController($uibModalInstance, entity, DlListing) {
        var vm = this;

        vm.dlListing = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Listing.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
