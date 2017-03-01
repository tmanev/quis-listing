(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('LanguageDetailController', LanguageDetailController);

    LanguageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Language'];

    function LanguageDetailController($scope, $rootScope, $stateParams, previousState, entity, Language) {
        var vm = this;

        vm.language = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('quisListingApp:languageUpdate', function(event, result) {
            vm.language = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
