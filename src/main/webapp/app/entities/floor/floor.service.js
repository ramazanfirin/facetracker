(function() {
    'use strict';
    angular
        .module('facetrackerApp')
        .factory('Floor', Floor);

    Floor.$inject = ['$resource'];

    function Floor ($resource) {
        var resourceUrl =  'api/floors/:id';

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
