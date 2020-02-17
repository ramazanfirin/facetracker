(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListRecordItemDetailController', BlackListRecordItemDetailController);

    BlackListRecordItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BlackListRecordItem', 'Record', 'BlackListPerson'];

    function BlackListRecordItemDetailController($scope, $rootScope, $stateParams, previousState, entity, BlackListRecordItem, Record, BlackListPerson) {
        var vm = this;

        vm.blackListRecordItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('facetrackerApp:blackListRecordItemUpdate', function(event, result) {
            vm.blackListRecordItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
