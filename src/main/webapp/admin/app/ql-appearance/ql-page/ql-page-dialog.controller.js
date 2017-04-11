(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlPageDialogController', QlPageDialogController)
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

    QlPageDialogController.$inject = ['$http', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
        'QlPage', 'AuthServerProvider'];

    function QlPageDialogController($http, $timeout, $scope, $stateParams, $uibModalInstance, entity,
                                       QlPage, AuthServerProvider) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.qlPage = entity;
        vm.clear = clear;
        vm.save = save;

        $scope.tinymceOptions = {
            menubar: false,
            plugins: 'code',
            toolbar: 'undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image | code',
        };

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.qlPage.id !== null) {
                QlPage.update(vm.qlPage, onSaveSuccess, onSaveError);
            } else {
                QlPage.save(vm.qlPage, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quisListingApp:qlPageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
