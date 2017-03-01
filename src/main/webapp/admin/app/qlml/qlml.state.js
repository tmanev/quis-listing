(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('qlml', {
            abstract: true,
            parent: 'app'
        });
    }
})();
