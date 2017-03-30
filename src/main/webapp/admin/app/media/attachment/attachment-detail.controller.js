(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('AttachmentDetailController', AttachmentDetailController);

    AttachmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Attachment'];

    function AttachmentDetailController($scope, $rootScope, $stateParams, previousState, entity, Attachment) {
        var vm = this;

        vm.attachment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:attachmentUpdate', function (event, result) {
            vm.attachment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
