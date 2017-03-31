(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('DlLocationSearch', DlLocationSearch);

    DlLocationSearch.$inject = ['$resource'];

    function DlLocationSearch($resource) {
        var resourceUrl = 'api/_search/dl-locations/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true}
        });
    }
})();
