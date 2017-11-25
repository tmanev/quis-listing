(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlListingDisapproveController', DlListingDisapproveController);

    DlListingDisapproveController.$inject = ['$uibModalInstance', 'entity', 'DlListing'];

    function DlListingDisapproveController($uibModalInstance, entity, DlListing) {
        var vm = this;

        vm.dlListing = entity;
        vm.message = null;
        vm.clear = clear;
        vm.confirmDisapprove = confirmDisapprove;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDisapprove(id) {
            DlListing.disapprove({id: id}, {
                    message: vm.message
                },
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
