(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('DlContentFieldGroupSearch', DlContentFieldGroupSearch);

    DlContentFieldGroupSearch.$inject = ['$resource'];

    function DlContentFieldGroupSearch($resource) {
        var resourceUrl =  'api/_search/dl-content-field-groups/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
