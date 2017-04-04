(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldConfigDialogController', DlContentFieldConfigDialogController)
    ;

    DlContentFieldConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DlContentField', 'AlertService'];

    function DlContentFieldConfigDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, DlContentField, AlertService) {
        var vm = this;
        vm.predicate = 'id';

        vm.dlContentField = entity;
        vm.dlContentFieldOption = {};
        vm.idReferenceCounter = 0;
        vm.clear = clear;
        vm.save = save;
        vm.initDlContentFieldOptionModel = initDlContentFieldOptionModel;
        vm.selectDeleteItem = selectDeleteItem;
        vm.selectAddItem = selectAddItem;

        initDlContentFieldOptionModel();

        function selectDeleteItem(selectionItem) {
            vm.dlContentFieldOption.selectionItems.splice(vm.dlContentFieldOption.selectionItems.indexOf(selectionItem), 1);
        }

        function selectAddItem() {
            var nextId = ++vm.idReferenceCounter;
            vm.dlContentFieldOption.selectionItems.push({
                key: nextId,
                value: ''
            });
        }

        function initDlContentFieldOptionModel() {
            if (vm.dlContentField.options) {
                vm.dlContentFieldOption = JSON.parse(vm.dlContentField.options);
            } else {
                if (vm.dlContentField.type == 'string') {
                    vm.dlContentFieldOption = {
                        maxLength: 25,
                        regex: ''
                    }
                } else if (vm.dlContentField.type == 'select' || vm.dlContentField.type == 'checkbox') {
                    vm.dlContentFieldOption = {
                        selectionItems: []
                    }
                } else if (vm.dlContentField.type == 'number') {
                    vm.dlContentFieldOption = {
                        isInteger: true,
                        decimalSeparator: ',',
                        thousandsSeparator: '',
                        min: '',
                        max: ''
                    }
                } else if (vm.dlContentField.type == 'website') {
                    vm.dlContentFieldOption = {
                        isBlank: true,
                        isNoFollow: true,
                        useLinkText: true,
                        defaultLinkText: '',
                        useDefaultLinkText: false
                    }
                }
            }

            if (vm.dlContentField.type == 'select' || vm.dlContentField.type == 'checkbox') {
                for (var i = 0; i < vm.dlContentFieldOption.selectionItems.length; i++) {
                    if (vm.dlContentFieldOption.selectionItems[i].key > vm.idReferenceCounter) {
                        vm.idReferenceCounter = vm.dlContentFieldOption.selectionItems[i].key;
                    }
                }
            }
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.dlContentField.id !== null) {
                // do the parsing
                vm.dlContentField.options = angular.toJson(vm.dlContentFieldOption);
                DlContentField.update(vm.dlContentField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quisListingApp:dlContentFieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
