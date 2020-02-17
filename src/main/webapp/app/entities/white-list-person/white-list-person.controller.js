(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListPersonController', WhiteListPersonController);

    WhiteListPersonController.$inject = ['WhiteListPerson'];

    function WhiteListPersonController(WhiteListPerson) {

        var vm = this;

        vm.whiteListPeople = [];

        loadAll();

        function loadAll() {
            WhiteListPerson.query(function(result) {
                vm.whiteListPeople = result;
                vm.searchQuery = null;
            });
        }
    }
})();
