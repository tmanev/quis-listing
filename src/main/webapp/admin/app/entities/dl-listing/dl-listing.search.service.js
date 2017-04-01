(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('DlListingSearch', DlListingSearch);

    DlListingSearch.$inject = ['$resource'];

    function DlListingSearch($resource) {
        var resourceUrl =  'api/_search/dl-listings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
