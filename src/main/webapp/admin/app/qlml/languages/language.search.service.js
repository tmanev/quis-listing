(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('LanguageSearch', LanguageSearch);

    LanguageSearch.$inject = ['$resource'];

    function LanguageSearch($resource) {
        var resourceUrl =  'api/_search/language/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
