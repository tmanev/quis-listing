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
            {key: 'string', value: 'Text string'},
            {key: 'textarea', value: 'Textarea'},
            {key: 'number', value: 'Digital value'},
            {key: 'select', value: 'Select list'},
            {key: 'radio', value: 'Radio buttons'},
            {key: 'checkbox', value: 'Checkboxes'},
            {key: 'website', value: 'Website URL'},
            {key: 'email', value: 'Email'},
            {key: 'datetime', value: 'Date-Time'},
            {key: 'price', value: 'Price'},
            {key: 'hours', value: 'Opening hours'}
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
            if (vm.dlContentField.categories) {
                // make the parsing
                var obj = JSON.parse(vm.dlContentField.categories);
                var arrayLength = obj.length;
                for (var i = 0; i < arrayLength; i++) {
                    var dlCategory = findDlCategory(obj[i].id);
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
                var objToParse = [];
                for (var i = 0; i < arrayLength; i++) {
                    var selectedDlCategory = vm.selectedCategories[i];
                    var obj = {
                        id: selectedDlCategory.id,
                        languageCode: selectedDlCategory.languageCode
                    };
                    objToParse.push(obj);
                }
                vm.dlContentField.categories = JSON.stringify(objToParse);
            }
        }

        function clearDlCategoriesByLanguageCode(languageCode) {
            if (vm.dlContentField.categories) {
                var objDlCategories = JSON.parse(vm.dlContentField.categories);
                for (var i = objDlCategories.length - 1; i >= 0; i--) {
                    if (objDlCategories[i].languageCode === languageCode) {
                        objDlCategories.splice(i, 1);
                    }
                }
                vm.dlContentField.categories = JSON.stringify(objDlCategories);
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
            console.log(vm.dlContentField.categories);
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
            vm.dlContentField.categories = null;
        }
    }
})();
