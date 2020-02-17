(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListPersonController', BlackListPersonController);

    BlackListPersonController.$inject = ['BlackListPerson'];

    function BlackListPersonController(BlackListPerson) {

        var vm = this;

        vm.blackListPeople = [];

        loadAll();

        function loadAll() {
            BlackListPerson.query(function(result) {
                vm.blackListPeople = result;
                vm.searchQuery = null;
            });
        }
    }
})();
