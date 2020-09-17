(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordSenseRapor2Controller', RecordSenseRapor2Controller);

    RecordSenseRapor2Controller.$inject = ['$state', 'DataUtils', 'RecordSense', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Person'];

    function RecordSenseRapor2Controller($state, DataUtils, RecordSense, ParseLinks, AlertService, paginationConstants, pagingParams,Person) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        
        vm.people = Person.query();
        vm.loadRecords = loadRecords;
        //vm.loadRecordsForUnknownPersons = loadRecordsUnknownPersons;
        vm.loadRecordsForKnownPersons = loadRecordsForKnownPersons;
        vm.getRecordsForReportDidntCome = getRecordsForReportDidntCome;
        
        var d = new Date();
        d.setHours(0);
        
        vm.startDate= d;

        
        var d2 =  new Date();
        d2.setHours(23);
        
        vm.endDate = d2;
        
        loadRecordsForKnownPersons();

        function loadAll () {
            RecordSense.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.recordSenses = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        
        
function loadRecords () {
        	
        	
			RecordSense.getRecords({
            	personId:vm.person.id,
            	startDate:vm.startDate,
                endDate:vm.endDate
            }, onSuccess, onError);
            
            function onSuccess(data, headers) {
                vm.recordSenses = data;
               
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
}


function loadRecordsForKnownPersons () {
	
	
	RecordSense.getRecordsForKnown2({
    	startDate:vm.startDate,
        endDate:vm.endDate
    }, onSuccess, onError);
    
    function onSuccess(data, headers) {
        vm.recordSenses = data;
       
    }
    function onError(error) {
        AlertService.error(error.data.message);
    }
}

function getRecordsForReportDidntCome () {
	
	
	RecordSense.getRecordsForReportDidntCome({
    	startDate:vm.startDate,
        endDate:vm.endDate
    }, onSuccess, onError);
    
    function onSuccess(data, headers) {
        vm.recordSenses = data;
       
    }
    function onError(error) {
        AlertService.error(error.data.message);
    }
}
        
    }
})();
