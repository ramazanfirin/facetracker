(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('ImageController', ImageController);

    ImageController.$inject = ['DataUtils', 'Image'];

    function ImageController(DataUtils, Image) {

        var vm = this;

        vm.images = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Image.query(function(result) {
                vm.images = result;
                vm.searchQuery = null;
            });
        }
    }
})();
