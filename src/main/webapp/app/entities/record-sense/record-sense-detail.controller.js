(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordSenseDetailController', RecordSenseDetailController);

    RecordSenseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'RecordSense', 'Device', 'Image'];

    function RecordSenseDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, RecordSense, Device, Image) {
        var vm = this;

        vm.recordSense = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('facetrackerApp:recordSenseUpdate', function(event, result) {
            vm.recordSense = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
