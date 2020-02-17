(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordController', RecordController);

    RecordController.$inject = ['DataUtils', 'Record'];

    function RecordController(DataUtils, Record) {

        var vm = this;

        vm.records = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Record.query(function(result) {
                vm.records = result;
                vm.searchQuery = null;
            });
        }
    }
})();
