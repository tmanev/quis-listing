(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlListingDialogController', DlListingDialogController)
        .controller('FileDestroyController', [
            '$scope', '$http',
            function ($scope, $http) {
                var file = $scope.file,
                    state;
                if (file.url) {
                    file.$state = function () {
                        return state;
                    };
                    file.$destroy = function () {
                        state = 'pending';
                        return $http({
                            url: file.deleteUrl,
                            method: file.deleteType
                        }).then(
                            function () {
                                state = 'resolved';
                                $scope.clear(file);
                            },
                            function () {
                                state = 'rejected';
                            }
                        );
                    };
                } else if (!file.$cancel && !file._index) {
                    file.$cancel = function () {
                        $scope.clear(file);
                    };
                }
            }
        ])
    ;

    DlListingDialogController.$inject = ['$http', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
        'DlListing', 'DlCategory', 'DlContentField', 'DlLocation', 'AuthServerProvider'];

    function DlListingDialogController($http, $timeout, $scope, $stateParams, $uibModalInstance, entity,
                                       DlListing, DlCategory, DlContentField, DlLocation, AuthServerProvider) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.dlListing = entity;
        vm.selectedDlCategoryId = null;
        if (vm.dlListing.dlCategories && vm.dlListing.dlCategories.length > 0) {
            vm.selectedDlCategoryId = vm.dlListing.dlCategories[0].id;
        }

        vm.selectedDlLocationId = null;
        if (vm.dlListing.dlLocations && vm.dlListing.dlLocations.length > 0) {
            vm.selectedDlLocationId = vm.dlListing.dlLocations[0].id;
        }

        vm.clear = clear;
        vm.fileUploadOptions = {
            headers: {
                'Authorization': 'Bearer ' + AuthServerProvider.getToken()
            },
            url: '/api/admin/attachments/upload',
            sequentialUploads: true
        };
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        vm.onDlCategorySelectCallback = function ($item, $model) {
            console.log("Item:");
            console.log($item);
            console.log("Model:");
            console.log($model);
            if (vm.dlListing.dlCategories == null) {
                vm.dlListing.dlCategories = [];
            }
            vm.dlListing.dlCategories[0] = JSON.parse(angular.toJson($item));
        };

        vm.onDlLocationSelectCallback = function ($item, $model) {
            console.log("Item:");
            console.log($item);
            console.log("Model:");
            console.log($model);
            if (vm.dlListing.dlLocations == null) {
                vm.dlListing.dlLocations = [];
            }
            vm.dlListing.dlLocations[0] = JSON.parse(angular.toJson($item));
        };

        vm.loadUploadedFiles = loadUploadedFiles;
        vm.loadCategories = loadCategories;
        vm.loadDlContentFields = loadDlContentFields;
        vm.loadDlLocations = loadDlLocations;

        $scope.tinymceOptions = {
            menubar: false,
            plugins: 'code',
            toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | code',
        };

        // loadUploadedFiles();
        loadCategories();
        loadDlContentFields();
        loadDlLocations();

        function loadCategories() {
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
                vm.dlCategoryTotalItems = headers('X-Total-Count');
                vm.dlCategoryQueryCount = vm.dlCategoryTotalItems;

                vm.dlCategories = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadDlLocations() {
            DlLocation.query({
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
                vm.dlLocationTotalItems = headers('X-Total-Count');
                vm.dlLocationQueryCount = vm.dlLocationTotalItems;

                vm.dlLocations = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadUploadedFiles() {
            if (vm.dlListing.id !== null) {
                $scope.loadingFiles = true;
                $http.get(url)
                    .then(
                        function (response) {
                            $scope.loadingFiles = false;
                            $scope.queue = response.data.files || [];
                        },
                        function () {
                            $scope.loadingFiles = false;
                        }
                    );
            }
        }

        function loadDlContentFields() {
            DlContentField.query({
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.dlContentFieldTotalItems = headers('X-Total-Count');
                vm.dlContentFieldQueryCount = vm.dlCategoryTotalItems;

                for (var i = 0; i < data.length; i++) {
                    var dlContentField = data[i];

                    if (dlContentField.options) {
                        dlContentField.optionsModel = JSON.parse(dlContentField.options);
                    }

                    dlContentField.value = getValueFromContentField(dlContentField, vm.dlListing.dlListingFields);
                }

                vm.dlContentFields = data;
            }

            function getValueFromContentField(dlContentField, dlListingFields) {
                if (dlListingFields) {
                    for (let dlListingField of dlListingFields) {
                        if (dlListingField.id == dlContentField.id) {
                            if (dlContentField.type == 'checkbox') {
                                if (dlListingField.value) {
                                    return JSON.parse(dlListingField.value);
                                }
                            } else {
                                return dlListingField.value;
                            }
                        }
                    }
                }
                return null;
            }

            function onError(error) {
                AlertService.error(error.data.message);
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

            let dlListingFields = [];
            for (let dlContentField of vm.dlContentFields) {
                let value;
                if (dlContentField.type == 'checkbox') {
                    value = JSON.stringify(dlContentField.value);
                } else {
                    value = dlContentField.value;
                }
                let listingField = {
                    id: dlContentField.id,
                    value: value
                };
                dlListingFields.push(listingField)
            }

            vm.dlListing.dlListingFields = dlListingFields;

            if (vm.dlListing.id !== null) {
                DlListing.update(vm.dlListing, onSaveSuccessUpdate, onSaveError);
            } else {
                DlListing.save(vm.dlListing, onSaveSuccessCreate, onSaveError);
            }
        }

        function onSaveSuccessUpdate(result) {
            $scope.$emit('quisListingApp:dlListingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveSuccessCreate(result) {
            $scope.$emit('quisListingApp:dlListingUpdate', result);
            vm.dlListing = result;
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.expirationDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
