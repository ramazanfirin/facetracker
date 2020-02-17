(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListRecordItemController', BlackListRecordItemController);

    BlackListRecordItemController.$inject = ['BlackListRecordItem'];

    function BlackListRecordItemController(BlackListRecordItem) {

        var vm = this;

        vm.blackListRecordItems = [];

        loadAll();

        function loadAll() {
            BlackListRecordItem.query(function(result) {
                vm.blackListRecordItems = result;
                vm.searchQuery = null;
            });
        }
    }
})();
