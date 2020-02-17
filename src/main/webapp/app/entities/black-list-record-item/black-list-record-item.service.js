(function() {
    'use strict';
    angular
        .module('facetrackerApp')
        .factory('BlackListRecordItem', BlackListRecordItem);

    BlackListRecordItem.$inject = ['$resource'];

    function BlackListRecordItem ($resource) {
        var resourceUrl =  'api/black-list-record-items/:id';

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
