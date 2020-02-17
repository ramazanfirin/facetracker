(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListRecordItemDeleteController',WhiteListRecordItemDeleteController);

    WhiteListRecordItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'WhiteListRecordItem'];

    function WhiteListRecordItemDeleteController($uibModalInstance, entity, WhiteListRecordItem) {
        var vm = this;

        vm.whiteListRecordItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WhiteListRecordItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
