(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('QlPageSearch', QlPageSearch);

    QlPageSearch.$inject = ['$resource'];

    function QlPageSearch($resource) {
        var resourceUrl =  'api/_search/ql-pages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
