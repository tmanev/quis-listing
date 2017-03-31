(function () {
    'use strict';
    angular
        .module('quisListingApp')
        .factory('DlLocation', DlLocation);

    DlLocation.$inject = ['$resource', 'DateUtils'];

    function DlLocation($resource, DateUtils) {
        var resourceUrl = 'api/admin/dl-locations/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'activeLanguages': {
                url: resourceUrl + '/active-languages',
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
