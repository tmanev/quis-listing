(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlListingApproveController',DlListingApproveController);

    DlListingApproveController.$inject = ['$uibModalInstance', 'entity', 'DlListing'];

    function DlListingApproveController($uibModalInstance, entity, DlListing) {
        var vm = this;

        vm.dlListing = entity;
        vm.clear = clear;
        vm.confirmApprove = confirmApprove;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmApprove (id) {
            DlListing.approve({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
