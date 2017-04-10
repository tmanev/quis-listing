(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('QlStringSearch', QlStringSearch);

    QlStringSearch.$inject = ['$resource'];

    function QlStringSearch($resource) {
        var resourceUrl =  'api/_search/ql-string/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
