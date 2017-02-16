(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('ListingDialogController', ListingDialogController);

    ListingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Listing'];

    function ListingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Listing) {
        var vm = this;

        vm.listing = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.listing.id !== null) {
                Listing.update(vm.listing, onSaveSuccess, onSaveError);
            } else {
                Listing.save(vm.listing, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:listingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
