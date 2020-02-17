(function() {
    'use strict';
    angular
        .module('facetrackerApp')
        .factory('BlackListPerson', BlackListPerson);

    BlackListPerson.$inject = ['$resource'];

    function BlackListPerson ($resource) {
        var resourceUrl =  'api/black-list-people/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
