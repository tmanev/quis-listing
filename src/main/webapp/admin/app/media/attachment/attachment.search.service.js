(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('AttachmentSearch', AttachmentSearch);

    AttachmentSearch.$inject = ['$resource'];

    function AttachmentSearch($resource) {
        var resourceUrl =  'api/_search/attachment/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
