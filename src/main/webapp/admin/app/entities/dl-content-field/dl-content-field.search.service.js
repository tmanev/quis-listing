(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('DlContentFieldSearch', DlContentFieldSearch);

    DlContentFieldSearch.$inject = ['$resource'];

    function DlContentFieldSearch($resource) {
        var resourceUrl =  'api/_search/dl-content-fields/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
