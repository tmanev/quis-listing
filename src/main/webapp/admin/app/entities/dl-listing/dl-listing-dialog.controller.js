(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlListingDialogController', DlListingDialogController)
    ;

    DlListingDialogController.$inject = ['$http', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
        'DlListing', 'DlCategory', 'DlContentField', 'DlLocation', 'AuthServerProvider', 'Upload', 'AlertService',
        '$location', '$window'];

    function DlListingDialogController($http, $timeout, $scope, $stateParams, $uibModalInstance, entity,
                                       DlListing, DlCategory, DlContentField, DlLocation, AuthServerProvider, Upload, AlertService,
                                       location, $window) {
        var vm = this;
        vm.baseUrl = location.protocol() + "://" + location.host() + ":" + location.port();
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
        vm.upload = upload;
        vm.deleteAttachment = deleteAttachment;
        vm.log = "";

        vm.clear = clear;

        $scope.$watch('vm.uploadFiles', function () {
            vm.upload(vm.uploadFiles);
        });

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.approve = approve;
        vm.disapprove = disapprove;

        function approve() {
            DlListing.approve({id: vm.dlListing.id}, onSaveSuccessUpdate, onSaveError);
        }
        
        function disapprove() {
            DlListing.disapprove({id: vm.dlListing.id}, onSaveSuccessUpdate, onSaveError);
        }
        
        function deleteAttachment(attachmentId) {

            var confirm = $window.confirm("Confirm deletion of image!");
            if (confirm == true) {
                let data = {id: vm.dlListing.id, attachmentId: attachmentId};
                DlListing.deleteAttachment(data, onDeleteSuccess, onDeleteError);
            }

            function onDeleteSuccess(result) {
                vm.dlListing.attachments = result.attachments;
            }

            function onDeleteError(error) {
                AlertService.error(error.data.message);
            }
        }

        function upload(uploadFiles) {
            if (uploadFiles && uploadFiles.length) {
                Upload.upload({
                    url: '/api/admin/dl-listings/' + vm.dlListing.id + '/upload',
                    data: {
                        files: uploadFiles
                    }
                }).then(function (resp) {
                    $timeout(function () {
                        vm.log = 'file: ' +
                            resp.config.data.files[0].name +
                            ', Response: ' + JSON.stringify(resp.data) +
                            '\n' + vm.log;

                        vm.dlListing.attachments = resp.data.attachments;
                    });
                }, function (response) {
                    if (response.status > 0) {
                        AlertService.error(response.data.message);
                    }
                }, function (evt) {
                    var progressPercentage = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                    vm.log = 'progress: ' + progressPercentage +
                        '% ' + evt.config.data.files[0].name + '\n' +
                        vm.log;
                });
            }
        };

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
                            if (dlContentField.type == 'CHECKBOX') {
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
                if (dlContentField.type == 'CHECKBOX') {
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
