(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('NavMenuSearch', NavMenuSearch);

    NavMenuSearch.$inject = ['$resource'];

    function NavMenuSearch($resource) {
        var resourceUrl =  'api/_search/nav-menus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
