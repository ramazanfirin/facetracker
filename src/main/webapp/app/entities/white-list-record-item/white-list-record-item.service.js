(function() {
    'use strict';
    angular
        .module('facetrackerApp')
        .factory('WhiteListRecordItem', WhiteListRecordItem);

    WhiteListRecordItem.$inject = ['$resource'];

    function WhiteListRecordItem ($resource) {
        var resourceUrl =  'api/white-list-record-items/:id';

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
