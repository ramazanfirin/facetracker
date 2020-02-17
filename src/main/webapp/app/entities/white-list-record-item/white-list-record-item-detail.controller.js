(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListRecordItemDetailController', WhiteListRecordItemDetailController);

    WhiteListRecordItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WhiteListRecordItem', 'Record', 'WhiteListPerson'];

    function WhiteListRecordItemDetailController($scope, $rootScope, $stateParams, previousState, entity, WhiteListRecordItem, Record, WhiteListPerson) {
        var vm = this;

        vm.whiteListRecordItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('facetrackerApp:whiteListRecordItemUpdate', function(event, result) {
            vm.whiteListRecordItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
