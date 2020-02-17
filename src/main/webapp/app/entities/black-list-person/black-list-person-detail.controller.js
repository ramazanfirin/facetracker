(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListPersonDetailController', BlackListPersonDetailController);

    BlackListPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BlackListPerson', 'Image'];

    function BlackListPersonDetailController($scope, $rootScope, $stateParams, previousState, entity, BlackListPerson, Image) {
        var vm = this;

        vm.blackListPerson = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('facetrackerApp:blackListPersonUpdate', function(event, result) {
            vm.blackListPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
