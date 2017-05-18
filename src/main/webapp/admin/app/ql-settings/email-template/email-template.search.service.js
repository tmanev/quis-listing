(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('EmailTemplateSearch', EmailTemplateSearch);

    EmailTemplateSearch.$inject = ['$resource'];

    function EmailTemplateSearch($resource) {
        var resourceUrl =  'api/_search/email-templates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
