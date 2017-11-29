(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldConfigDialogController', DlContentFieldConfigDialogController)
    ;

    DlContentFieldConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DlContentField', 'AlertService', 'DlContentFieldItem'];

    function DlContentFieldConfigDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, DlContentField, AlertService, DlContentFieldItem) {
        var vm = this;
        vm.predicate = 'id';

        vm.dlContentField = entity;
        vm.dlContentFieldItems = [];
        vm.rootDlContentFieldItems = [];
        vm.parentDlContentFieldItem = null;
        vm.newDlContentFieldItem = {id: null, value: '', parent: null, children: null};
        vm.dlContentFieldOption = {};

        vm.clear = clear;
        vm.save = save;
        vm.initDlContentFieldOptionModel = initDlContentFieldOptionModel;
        vm.deleteItem = deleteItem;
        vm.saveItem = saveItem;
        vm.addDlContentFieldItem = addDlContentFieldItem;
        vm.onSelectParentItemCallback = onSelectParentItemCallback;
        vm.clearParent = clearParent;


        vm.loadItems = loadItems;
        vm.loadRootItems = loadRootItems;

        initDlContentFieldOptionModel();

        function addDlContentFieldItem() {
            let parent = null;
            if (vm.parentDlContentFieldItem!==null) {
                parent = {id: vm.parentDlContentFieldItem}
            }
            vm.newDlContentFieldItem.parent = parent;
            if (vm.newDlContentFieldItem.id !== null) {
                DlContentFieldItem.update({dlContentFieldId: vm.dlContentField.id}, vm.newDlContentFieldItem, onAddSuccess, onError);
            } else {
                DlContentFieldItem.save({dlContentFieldId: vm.dlContentField.id}, vm.newDlContentFieldItem, onAddSuccess, onError);
            }
        }

        function deleteItem(dlContentFieldItem) {
            DlContentFieldItem.delete({dlContentFieldId: vm.dlContentField.id, id: dlContentFieldItem.id}, onDeleteItemSuccess, onError);
        }

        function saveItem(dlContentFieldItem) {
            if (dlContentFieldItem.id !== null) {
                DlContentFieldItem.update({dlContentFieldId: vm.dlContentField.id}, dlContentFieldItem, onSuccess, onError);
            } else {
                DlContentFieldItem.save({dlContentFieldId: vm.dlContentField.id}, dlContentFieldItem, onSuccess, onError);
            }
        }

        function onDeleteItemSuccess(result) {
            vm.loadItems();
            if (vm.parentDlContentFieldItem===null) {
                vm.loadRootItems();
            }
        }

        function onAddSuccess(result) {
            vm.newDlContentFieldItem = {id: null, value: '', parent: null, children: null};
            vm.loadItems();
            if (vm.parentDlContentFieldItem===null) {
                vm.loadRootItems();
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
        
        function onSuccess() {

        }

        function initDlContentFieldOptionModel() {
            if (vm.dlContentField.type === 'STRING') {
                if (vm.dlContentField.options !== null && vm.dlContentField.options !== '') {
                    vm.dlContentFieldOption = JSON.parse(vm.dlContentField.options);
                } else {
                    vm.dlContentFieldOption = {
                        maxLength: 25,
                        regex: ''
                    };
                }
            } else if (vm.dlContentField.type === 'NUMBER') {
                if (vm.dlContentField.options !== null && vm.dlContentField.options !== '') {
                    vm.dlContentFieldOption = JSON.parse(vm.dlContentField.options);
                } else {
                    vm.dlContentFieldOption = {
                        isInteger: true,
                        decimalSeparator: ',',
                        thousandsSeparator: '',
                        min: '',
                        max: ''
                    }
                }

            } else if (vm.dlContentField.type === 'WEBSITE') {
                if (vm.dlContentField.options !== null && vm.dlContentField.options !== '') {
                    vm.dlContentFieldOption = JSON.parse(vm.dlContentField.options);
                } else {
                    vm.dlContentFieldOption = {
                        isBlank: true,
                        isNoFollow: true,
                        useLinkText: true,
                        defaultLinkText: '',
                        useDefaultLinkText: false
                    };
                }
            } else if (vm.dlContentField.type === 'SELECT' || vm.dlContentField.type === 'CHECKBOX') {
                vm.loadItems();
            } else if (vm.dlContentField.type === 'DEPENDENT_SELECT') {
                vm.loadItems();
                vm.loadRootItems();
            }
        }

        function loadItems() {
            let parentId = vm.parentDlContentFieldItem !== null ? vm.parentDlContentFieldItem : null;
            DlContentFieldItem.query({
                dlContentFieldId: vm.dlContentField.id,
                parentId: parentId,
                size: 500
            }, onItemsSuccess, onItemsError);
        }

        function loadRootItems() {
            DlContentFieldItem.query({dlContentFieldId: vm.dlContentField.id, parentId: null, size: 100}, onRootItemsSuccess, onRootItemsError);

            function onRootItemsSuccess(data, headers) {
                vm.rootDlContentFieldItems = data;
            }

            function onRootItemsError(error) {
                AlertService.error(error.data.message);
            }
        }

        function onSelectParentItemCallback($item, $model) {
            vm.loadItems();
        }
        
        function clearParent() {
            vm.parentDlContentFieldItem = null;
            vm.loadItems();
        }

        function onItemsSuccess(data, headers) {
            console.log("Succes retrievinf dl-content-field-items");
            vm.dlContentFieldItems = data;
        }

        function onItemsError(error) {
            AlertService.error(error.data.message);
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
