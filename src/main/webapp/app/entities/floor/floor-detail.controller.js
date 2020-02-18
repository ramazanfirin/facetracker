(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('FloorDetailController', FloorDetailController);

    FloorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Floor', 'Building'];

    function FloorDetailController($scope, $rootScope, $stateParams, previousState, entity, Floor, Building) {
        var vm = this;

        vm.floor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('facetrackerApp:floorUpdate', function(event, result) {
            vm.floor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
