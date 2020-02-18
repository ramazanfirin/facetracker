(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BuildingController', BuildingController);

    BuildingController.$inject = ['Building'];

    function BuildingController(Building) {

        var vm = this;

        vm.buildings = [];

        loadAll();

        function loadAll() {
            Building.query(function(result) {
                vm.buildings = result;
                vm.searchQuery = null;
            });
        }
    }
})();
