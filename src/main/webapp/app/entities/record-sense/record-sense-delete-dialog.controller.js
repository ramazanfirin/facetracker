(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordSenseDeleteController',RecordSenseDeleteController);

    RecordSenseDeleteController.$inject = ['$uibModalInstance', 'entity', 'RecordSense'];

    function RecordSenseDeleteController($uibModalInstance, entity, RecordSense) {
        var vm = this;

        vm.recordSense = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RecordSense.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
