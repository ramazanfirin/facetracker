(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('FloorDeleteController',FloorDeleteController);

    FloorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Floor'];

    function FloorDeleteController($uibModalInstance, entity, Floor) {
        var vm = this;

        vm.floor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Floor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
