(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlCategoryDialogController', DlCategoryDialogController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i<total; i++)
                    input.push(i);
                return input;
            };
        });

    DlCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DlCategory', 'AlertService'];

    function DlCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DlCategory, AlertService) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.person = {};

        vm.person.selectedValue = {};
        vm.person.selectedSingle = 'Samantha';
        vm.person.selectedSingleKey = '5';
        // To run the demos with a preselected person object, uncomment the line below.
        //vm.person.selected = vm.person.selectedValue;

        loadAll();

        vm.onSelectCallback = function ($item, $model) {
            console.log("Item:");
            console.log($item);
            console.log("Model:");
            console.log($model);
        };

        function loadAll () {

            DlCategory.query({
                sort: sort(),
                languageCode: entity.languageCode
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.dlCategories = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.dlCategory = entity;
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
            if (vm.dlCategory.id !== null) {
                DlCategory.update(vm.dlCategory, onSaveSuccess, onSaveError);
            } else {
                DlCategory.save(vm.dlCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:dlCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
