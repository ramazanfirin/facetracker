(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListRecordItemController', WhiteListRecordItemController);

    WhiteListRecordItemController.$inject = ['WhiteListRecordItem'];

    function WhiteListRecordItemController(WhiteListRecordItem) {

        var vm = this;

        vm.whiteListRecordItems = [];

        loadAll();

        function loadAll() {
            WhiteListRecordItem.query(function(result) {
                vm.whiteListRecordItems = result;
                vm.searchQuery = null;
            });
        }
    }
})();
