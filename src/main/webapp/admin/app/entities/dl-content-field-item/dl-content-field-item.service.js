(function() {
    'use strict';
    angular
        .module('quisListingApp')
        .factory('DlContentFieldItem', DlContentFieldItem);

    DlContentFieldItem.$inject = ['$resource', 'DateUtils'];

    function DlContentFieldItem ($resource, DateUtils) {
        var resourceUrl =  'api/admin/dl-content-fields/:dlContentFieldId/dl-content-field-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
