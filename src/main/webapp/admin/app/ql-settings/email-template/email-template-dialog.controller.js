(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailTemplateDialogController', EmailTemplateDialogController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i<total; i++)
                    input.push(i);
                return input;
            };
        });

    EmailTemplateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmailTemplate', 'AlertService'];

    function EmailTemplateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmailTemplate, AlertService) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.emailTemplate = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.emailTemplate.id !== null) {
                EmailTemplate.update(vm.emailTemplate, onSaveSuccess, onSaveError);
            } else {
                EmailTemplate.save(vm.emailTemplate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:emailTemplateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
