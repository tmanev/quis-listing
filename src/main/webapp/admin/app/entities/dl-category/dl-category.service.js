(function() {
    'use strict';
    angular
        .module('quisListingApp')
        .factory('DlCategory', DlCategory);

    DlCategory.$inject = ['$resource', 'DateUtils'];

    function DlCategory ($resource, DateUtils) {
        var resourceUrl =  'api/admin/dl-categories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'activeLanguages': {
                url: resourceUrl+'/active-languages',
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
            'getByTranslation': {
                method: 'GET',
                url: 'api/admin/dl-categories/by-translation/:id',
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
            },
            'bindTranslation': {
                url: 'api/admin/dl-categories/bind-categories',
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
