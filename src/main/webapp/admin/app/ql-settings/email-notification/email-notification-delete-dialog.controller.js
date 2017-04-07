(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailNotificationDeleteController',EmailNotificationDeleteController);

    EmailNotificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmailNotification'];

    function EmailNotificationDeleteController($uibModalInstance, entity, EmailNotification) {
        var vm = this;

        vm.emailNotification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EmailNotification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
