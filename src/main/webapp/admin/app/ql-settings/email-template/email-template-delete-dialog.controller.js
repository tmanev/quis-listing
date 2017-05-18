(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailTemplateDeleteController',EmailTemplateDeleteController);

    EmailTemplateDeleteController.$inject = ['$uibModalInstance', 'entity', 'EmailTemplate'];

    function EmailTemplateDeleteController($uibModalInstance, entity, EmailTemplate) {
        var vm = this;

        vm.emailTemplate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EmailTemplate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
