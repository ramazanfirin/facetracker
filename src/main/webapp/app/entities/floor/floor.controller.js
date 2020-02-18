(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('FloorController', FloorController);

    FloorController.$inject = ['Floor'];

    function FloorController(Floor) {

        var vm = this;

        vm.floors = [];

        loadAll();

        function loadAll() {
            Floor.query(function(result) {
                vm.floors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
