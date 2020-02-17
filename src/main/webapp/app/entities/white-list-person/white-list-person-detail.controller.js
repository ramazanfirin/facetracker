(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListPersonDetailController', WhiteListPersonDetailController);

    WhiteListPersonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WhiteListPerson', 'Image'];

    function WhiteListPersonDetailController($scope, $rootScope, $stateParams, previousState, entity, WhiteListPerson, Image) {
        var vm = this;

        vm.whiteListPerson = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('facetrackerApp:whiteListPersonUpdate', function(event, result) {
            vm.whiteListPerson = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
