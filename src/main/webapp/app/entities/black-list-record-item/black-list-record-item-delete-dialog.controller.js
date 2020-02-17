(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListRecordItemDeleteController',BlackListRecordItemDeleteController);

    BlackListRecordItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'BlackListRecordItem'];

    function BlackListRecordItemDeleteController($uibModalInstance, entity, BlackListRecordItem) {
        var vm = this;

        vm.blackListRecordItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BlackListRecordItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
