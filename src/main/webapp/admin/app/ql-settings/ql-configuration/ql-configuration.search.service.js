(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('QlConfigurationSearch', QlConfigurationSearch);

    QlConfigurationSearch.$inject = ['$resource'];

    function QlConfigurationSearch($resource) {
        var resourceUrl =  'api/_search/ql-configurations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
