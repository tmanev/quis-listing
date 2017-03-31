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

    AttachmentAddController.$inject = ['$scope', '$uibModalInstance', 'AuthServerProvider'];

    function AttachmentAddController($scope, $uibModalInstance, AuthServerProvider) {
        var vm = this;

        vm.ok = ok;

        vm.fileUploadOptions = {
            headers: {
                'Authorization': 'Bearer ' + AuthServerProvider.getToken()
            },
            url: '/api/admin/attachments/upload',
            sequentialUploads: true
        };

        function ok() {
            vm.isSaving = true;
            $scope.$emit('quisListingApp:attachmentUpdate');
            $uibModalInstance.close();
            vm.isSaving = false;
        }

    }
})();
