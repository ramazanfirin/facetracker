(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('DeviceController', DeviceController);

    DeviceController.$inject = ['Device'];

    function DeviceController(Device) {

        var vm = this;

        vm.devices = [];

        loadAll();

        function loadAll() {
            Device.query(function(result) {
                vm.devices = result;
                vm.searchQuery = null;
            });
        }
    }
})();
