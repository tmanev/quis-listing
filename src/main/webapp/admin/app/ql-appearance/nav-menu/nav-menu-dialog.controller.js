(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('NavMenuDialogController', NavMenuDialogController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i<total; i++)
                    input.push(i);
                return input;
            };
        });

    NavMenuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NavMenu', 'AlertService'];

    function NavMenuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NavMenu, AlertService) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.navMenu = entity;
        vm.clear = clear;
        vm.save = save;

        loadAll();

        vm.onSelectCallback = function ($item, $model) {
            console.log("Item:");
            console.log($item);
            console.log("Model:");
            console.log($model);
        };

        function loadAll () {
            NavMenu.query({
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
                vm.navMenus = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.navMenu.id !== null) {
                NavMenu.update(vm.navMenu, onSaveSuccess, onSaveError);
            } else {
                NavMenu.save(vm.navMenu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:navMenuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
