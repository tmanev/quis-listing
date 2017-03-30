(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('AttachmentAddController', AttachmentAddController)
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

    AttachmentAddController.$inject = ['$scope', '$uibModalInstance', 'entity', 'Attachment', 'AuthServerProvider'];

    function AttachmentAddController($scope, $uibModalInstance, entity, Attachment, AuthServerProvider) {
        var vm = this;

        vm.attachment = entity;

        vm.clear = clear;
        vm.add = add;

        vm.fileUploadOptions = {
            headers: {
              'Authorization': 'Bearer ' + AuthServerProvider.getToken()
            },
            url: '/api/admin/attachments/upload',
            sequentialUploads: true
        };

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function add() {
            vm.isSaving = true;
            if (vm.attachment.id !== null) {
                Attachment.update(vm.attachment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('quisListingApp:attachmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
