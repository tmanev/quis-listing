(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('ListingDeleteController',ListingDeleteController);

    ListingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Listing'];

    function ListingDeleteController($uibModalInstance, entity, Listing) {
        var vm = this;

        vm.listing = entity;
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
