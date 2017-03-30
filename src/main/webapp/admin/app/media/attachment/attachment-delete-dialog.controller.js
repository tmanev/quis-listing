(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('AttachmentDeleteController', AttachmentDeleteController);

    AttachmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Attachment'];

    function AttachmentDeleteController($uibModalInstance, entity, Attachment) {
        var vm = this;

        vm.attachment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Attachment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
