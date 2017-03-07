(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('DataLanguageHub', DataLanguageHub);

    function DataLanguageHub () {
        var savedData = {
            selectedLanguageCode: 'en'
        };
        function set(data) {
            savedData = data;
        }

        function get() {
            return savedData;
        }

        return {
            set: set,
            get: get
        }
    }
})();
