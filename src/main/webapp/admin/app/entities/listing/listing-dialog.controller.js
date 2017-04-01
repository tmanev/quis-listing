(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('ListingDialogController', ListingDialogController)
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

    ListingDialogController.$inject = ['$http', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
        'Listing', 'DlCategory'];

    function ListingDialogController($http, $timeout, $scope, $stateParams, $uibModalInstance, entity, Listing, DlCategory) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.listing = entity;
        vm.clear = clear;
        vm.fileUploadOptions = {
            url: '/api/admin/files'
        };
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        vm.loadUploadedFiles = loadUploadedFiles;
        vm.loadCategories = loadCategories;
        vm.parentId = null;

        $scope.tinymceOptions = {
            menubar: false,
            plugins: 'code',
            toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | code',
        };

        loadUploadedFiles();
        loadCategories();

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
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.dlCategories = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadUploadedFiles() {
            if (vm.listing.id !== null) {
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

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.listing.id !== null) {
                Listing.update(vm.listing, onSaveSuccess, onSaveError);
            } else {
                Listing.save(vm.listing, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quisListingApp:listingUpdate', result);
            $uibModalInstance.close(result);
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
