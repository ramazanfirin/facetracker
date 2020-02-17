(function() {
    'use strict';
    angular
        .module('facetrackerApp')
        .factory('WhiteListPerson', WhiteListPerson);

    WhiteListPerson.$inject = ['$resource'];

    function WhiteListPerson ($resource) {
        var resourceUrl =  'api/white-list-people/:id';

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
