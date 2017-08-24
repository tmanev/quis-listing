(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlContentFieldDetailController', DlContentFieldDetailController)
        .filter('range', function () {
            return function (input, total) {
                total = parseInt(total);
                for (var i = 0; i < total; i++)
                    input.push(i);
                return input;
            };
        });

    DlContentFieldDetailController.$inject = ['$scope', '$rootScope', '$state', '$stateParams', 'previousState',
        'entity', 'DlContentField', 'DlCategory', 'DataLanguageHub', 'Language', 'AlertService'];

    function DlContentFieldDetailController($scope, $rootScope, $state, $stateParams, previousState,
                                            entity, DlContentField, DlCategory, DataLanguageHub, Language, AlertService) {
        var vm = this;
        vm.dataLanguageHub = DataLanguageHub.get();

        vm.dlContentField = entity;
        vm.previousState = previousState.name;

        vm.save = save;
        vm.onLanguageChange = onLanguageChange;
        vm.onLanguageClick = onLanguageClick;
        vm.clearDlCategorySelection = clearDlCategorySelection;

        vm.selectedLanguageCode = vm.dataLanguageHub.selectedLanguageCode;
        vm.selectedCategories = [];
        vm.activeLanguages = [
            {code: "en", englishName: "English", count: 0}
        ];

        vm.contentTypes = [
            {key: '', value: '- Select field type -'},
            {key: 'STRING', value: 'Text string'},
            {key: 'TEXT_AREA', value: 'Textarea'},
            {key: 'NUMBER', value: 'Digital value'},
            {key: 'SELECT', value: 'Select list'},
            {key: 'DEPENDENT_SELECT', value: 'Dependent select list'},
            {key: 'RADIO', value: 'Radio buttons'},
            {key: 'CHECKBOX', value: 'Checkboxes'},
            {key: 'WEBSITE', value: 'Website URL'},
            {key: 'EMAIL', value: 'Email'},
            {key: 'OPEN_HOURS', value: 'Opening hours'}
        ];

        loadActiveLanguages();
        loadAllCategoriesByLanguage();

        function loadActiveLanguages() {
            DlCategory.activeLanguages({}, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.activeLanguages = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function fillDlCategoriesSelection() {
            vm.selectedCategories = [];
            if (vm.dlContentField.dlCategories) {
                // make the parsing
                var arrayLength = vm.dlContentField.dlCategories.length;
                for (var i = 0; i < arrayLength; i++) {
                    var dlCategory = findDlCategory(vm.dlContentField.dlCategories[i].id);
                    if (dlCategory) {
                        vm.selectedCategories.push(dlCategory)
                    }
                }
            }
        }

        function parseDlCategoriesSelection(languageCode) {
            var arrayLength = vm.selectedCategories.length;
            clearDlCategoriesByLanguageCode(languageCode);
            if (arrayLength > 0) {
                if (vm.dlContentField.dlCategories == null){
                    vm.dlContentField.dlCategories = [];
                }
                for (var i = 0; i < arrayLength; i++) {
                    vm.dlContentField.dlCategories.push(vm.selectedCategories[i]);
                }
            }
        }

        function clearDlCategoriesByLanguageCode(languageCode) {
            if (vm.dlContentField.dlCategories) {
                for (var i = vm.dlContentField.dlCategories.length - 1; i >= 0; i--) {
                    if (vm.dlContentField.dlCategories[i].languageCode === languageCode) {
                        vm.dlContentField.dlCategories.splice(i, 1);
                    }
                }
            }
        }

        function findDlCategory(id) {
            var arrayLength = vm.dlCategories.length;
            for (var i = 0; i < arrayLength; i++) {
                if (id == vm.dlCategories[i].id) {
                    return vm.dlCategories[i];
                }
            }
            return null;
        }

        function loadAllCategoriesByLanguage() {
            DlCategory.query({
                languageCode: vm.selectedLanguageCode
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.dlCategories = data;
                fillDlCategoriesSelection();
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.onSelectCallback = function ($item, $model) {
            console.log("Item:");
            console.log($item);
            console.log("Model:");
            console.log($model);
            console.log(vm.dlContentField.dlCategories);
        };

        function save() {
            vm.isSaving = true;
            parseDlCategoriesSelection();
            if (vm.dlContentField.id !== null) {
                DlContentField.update(vm.dlContentField, onSaveSuccess, onSaveError);
            } else {
                DlContentField.save(vm.dlContentField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quisListingApp:dlContentFieldUpdate', result);
            vm.isSaving = false;
            $state.go('dl-content-fields');
        }

        function onSaveError(error) {
            vm.isSaving = false;
            AlertService.error(error.data.message);
        }

        function onLanguageChange(oldLanguage) {
            vm.dataLanguageHub.selectedLanguageCode = vm.selectedLanguageCode;
            parseDlCategoriesSelection(oldLanguage);
            loadAllCategoriesByLanguage();
        }

        function onLanguageClick() {

        }

        function clearDlCategorySelection() {
            vm.selectedCategories = [];
            vm.dlContentField.dlCategories = null;
        }
    }
})();
