(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordSenseDialogController', RecordSenseDialogController);

    RecordSenseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'RecordSense', 'Device', 'Image'];

    function RecordSenseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, RecordSense, Device, Image) {
        var vm = this;

        vm.recordSense = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.devices = Device.query();
        vm.images = Image.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.recordSense.id !== null) {
                RecordSense.update(vm.recordSense, onSaveSuccess, onSaveError);
            } else {
                RecordSense.save(vm.recordSense, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('facetrackerApp:recordSenseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.insert = false;
        vm.datePickerOpenStatus.fileSentDate = false;
        vm.datePickerOpenStatus.fileCreationDate = false;
        vm.datePickerOpenStatus.processStartDate = false;
        vm.datePickerOpenStatus.processFinishDate = false;

        vm.setAfid = function ($file, recordSense) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        recordSense.afid = base64Data;
                        recordSense.afidContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
