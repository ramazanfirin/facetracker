(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListPersonDeleteController',BlackListPersonDeleteController);

    BlackListPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'BlackListPerson'];

    function BlackListPersonDeleteController($uibModalInstance, entity, BlackListPerson) {
        var vm = this;

        vm.blackListPerson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BlackListPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
