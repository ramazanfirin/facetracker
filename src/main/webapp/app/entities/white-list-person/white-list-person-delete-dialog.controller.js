(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListPersonDeleteController',WhiteListPersonDeleteController);

    WhiteListPersonDeleteController.$inject = ['$uibModalInstance', 'entity', 'WhiteListPerson'];

    function WhiteListPersonDeleteController($uibModalInstance, entity, WhiteListPerson) {
        var vm = this;

        vm.whiteListPerson = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WhiteListPerson.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
