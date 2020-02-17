(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordDetailController', RecordDetailController);

    RecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Record', 'Device', 'Image'];

    function RecordDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Record, Device, Image) {
        var vm = this;

        vm.record = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('facetrackerApp:recordUpdate', function(event, result) {
            vm.record = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
