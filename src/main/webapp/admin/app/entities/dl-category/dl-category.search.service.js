(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('DlCategorySearch', DlCategorySearch);

    DlCategorySearch.$inject = ['$resource'];

    function DlCategorySearch($resource) {
        var resourceUrl =  'api/_search/dl-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
