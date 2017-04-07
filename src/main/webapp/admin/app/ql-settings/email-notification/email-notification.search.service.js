(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('EmailNotificationSearch', EmailNotificationSearch);

    EmailNotificationSearch.$inject = ['$resource'];

    function EmailNotificationSearch($resource) {
        var resourceUrl =  'api/_search/email-notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
