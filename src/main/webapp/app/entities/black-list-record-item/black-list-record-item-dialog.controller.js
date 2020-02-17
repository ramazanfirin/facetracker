(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListRecordItemDialogController', BlackListRecordItemDialogController);

    BlackListRecordItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BlackListRecordItem', 'Record', 'BlackListPerson'];

    function BlackListRecordItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BlackListRecordItem, Record, BlackListPerson) {
        var vm = this;

        vm.blackListRecordItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.records = Record.query();
        vm.blacklistpeople = BlackListPerson.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.blackListRecordItem.id !== null) {
                BlackListRecordItem.update(vm.blackListRecordItem, onSaveSuccess, onSaveError);
            } else {
                BlackListRecordItem.save(vm.blackListRecordItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('facetrackerApp:blackListRecordItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
