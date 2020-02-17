(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('ImageDialogController', ImageDialogController);

    ImageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Image', 'Person'];

    function ImageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Image, Person) {
        var vm = this;

        vm.image = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.people = Person.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.image.id !== null) {
                Image.update(vm.image, onSaveSuccess, onSaveError);
            } else {
                Image.save(vm.image, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('facetrackerApp:imageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, image) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        image.image = base64Data;
                        image.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
