(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('WhiteListPersonDialogController', WhiteListPersonDialogController);

    WhiteListPersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WhiteListPerson', 'Image'];

    function WhiteListPersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WhiteListPerson, Image) {
        var vm = this;

        vm.whiteListPerson = entity;
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
            if (vm.whiteListPerson.id !== null) {
                WhiteListPerson.update(vm.whiteListPerson, onSaveSuccess, onSaveError);
            } else {
                WhiteListPerson.save(vm.whiteListPerson, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('facetrackerApp:whiteListPersonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
