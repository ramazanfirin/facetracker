(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordDetailController', RecordDetailController);

    RecordDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Record', 'Device', 'Image'];

    function RecordDetailController($scope, $rootScope, $stateParams, previousState, entity, Record, Device, Image) {
        var vm = this;

        vm.record = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('facetrackerApp:recordUpdate', function(event, result) {
            vm.record = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
