(function() {
    'use strict';
    angular
        .module('facetrackerApp')
        .factory('RecordSense', RecordSense);

    RecordSense.$inject = ['$resource', 'DateUtils'];

    function RecordSense ($resource, DateUtils) {
        var resourceUrl =  'api/record-senses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.insert = DateUtils.convertDateTimeFromServer(data.insert);
                        data.fileSentDate = DateUtils.convertDateTimeFromServer(data.fileSentDate);
                        data.fileCreationDate = DateUtils.convertDateTimeFromServer(data.fileCreationDate);
                        data.processStartDate = DateUtils.convertDateTimeFromServer(data.processStartDate);
                        data.processFinishDate = DateUtils.convertDateTimeFromServer(data.processFinishDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
