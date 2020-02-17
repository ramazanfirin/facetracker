(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordController', RecordController);

    RecordController.$inject = ['Record'];

    function RecordController(Record) {

        var vm = this;

        vm.records = [];

        loadAll();

        function loadAll() {
            Record.query(function(result) {
                vm.records = result;
                vm.searchQuery = null;
            });
        }
    }
})();
