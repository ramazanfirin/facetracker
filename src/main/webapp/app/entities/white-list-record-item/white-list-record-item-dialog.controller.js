(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListRecordItemDialogController', WhiteListRecordItemDialogController);

    WhiteListRecordItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WhiteListRecordItem', 'Record', 'WhiteListPerson'];

    function WhiteListRecordItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WhiteListRecordItem, Record, WhiteListPerson) {
        var vm = this;

        vm.whiteListRecordItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.records = Record.query();
        vm.whitelistpeople = WhiteListPerson.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.whiteListRecordItem.id !== null) {
                WhiteListRecordItem.update(vm.whiteListRecordItem, onSaveSuccess, onSaveError);
            } else {
                WhiteListRecordItem.save(vm.whiteListRecordItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('facetrackerApp:whiteListRecordItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
