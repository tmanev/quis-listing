(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailNotificationDialogController', EmailNotificationDialogController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i<total; i++)
                    input.push(i);
                return input;
            };
        });

    EmailNotificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmailNotification', 'AlertService'];

    function EmailNotificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmailNotification, AlertService) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.emailNotification = entity;
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
            if (vm.emailNotification.id !== null) {
                EmailNotification.update(vm.emailNotification, onSaveSuccess, onSaveError);
            } else {
                EmailNotification.save(vm.emailNotification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:emailNotificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
