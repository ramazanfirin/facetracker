(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('BlackListPersonDialogController', BlackListPersonDialogController);

    BlackListPersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BlackListPerson', 'Image'];

    function BlackListPersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BlackListPerson, Image) {
        var vm = this;

        vm.blackListPerson = entity;
        vm.clear = clear;
        vm.save = save;
        vm.images = Image.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.blackListPerson.id !== null) {
                BlackListPerson.update(vm.blackListPerson, onSaveSuccess, onSaveError);
            } else {
                BlackListPerson.save(vm.blackListPerson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('facetrackerApp:blackListPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
