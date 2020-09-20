(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordSenseRapor3Controller', RecordSenseRapor3Controller);

    RecordSenseRapor3Controller.$inject = ['$state', 'DataUtils', 'RecordSense', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Person'];

    function RecordSenseRapor3Controller($state, DataUtils, RecordSense, ParseLinks, AlertService, paginationConstants, pagingParams,Person) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        
        vm.people = Person.query();
        //vm.loadRecords = loadRecords;
        //vm.loadRecordsForUnknownPersons = loadRecordsUnknownPersons;
       //vm.loadRecordsForKnownPersons = loadRecordsForKnownPersons;
        //vm.getRecordsForReportDidntCome = getRecordsForReportDidntCome;
        vm.getRecordsForReportForEnrtyChartData = getRecordsForReportForEnrtyChartData;
        
        var d = new Date();
        d.setHours(0);
        
        vm.startDate= d;

        
        var d2 =  new Date();
        d2.setHours(23);
        
        vm.endDate = d2;
        
        
        vm.labels = ["01", "02", "03", "04", "05", "06", "07","08", "09", "10"];
        vm.series = ['Series A'];
        vm.data = [
          [22212, 22639, 21825, 21034, 20815,22212, 22639, 21825, 21034, 20815]
         
        ];
        
        vm.options = {
        		 showAllTooltips: true,
        		scales: {
        	        yAxes: [{
        	          ticks: {
        	            userCallback: function(v) { return epoch_to_hh_mm_ss(v) },
        	            stepSize: 30 * 60
        	          }
        	        }]
        	      }, 
        		
        		tooltips:{
        	            enabled:true
        	        },
        	    legend: {
        	             display: true
        	        },
        	        
        	        tooltips: {
        	            callbacks: {
        	              label: function(tooltipItem, data) {
        	                return data.datasets[tooltipItem.datasetIndex].label + ': ' + epoch_to_hh_mm_ss(tooltipItem.yLabel)
        	              }
        	            }
        	          }
        	         
        	    
        	  };
        
        
        vm.colours = [{ // default
            "fillColor": "rgba(255, 255, 255, 1)",
            "strokeColor": "rgba(255,255,255,1)",
            "pointColor": "rgba(255,255,255,1)",
            "pointStrokeColor": "#fff",
            "pointHighlightFill": "#fff",
            "pointHighlightStroke": "rgba(151,187,205,0.8)"
          }]
        ;
        //loadRecordsForKnownPersons();

        function epoch_to_hh_mm_ss(epoch) {
            return new Date(epoch*1000).toISOString().substr(11, 5)
          }
        
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
        
        
function getRecordsForReportForEnrtyChartData () {
        	
        	
			RecordSense.getRecordsForReportForEnrtyChartData({
            	personId:vm.person.id,
            	startDate:vm.startDate,
                endDate:vm.endDate
            }, onSuccess, onError);
            
            function onSuccess(data, headers) {
                vm.recordSenses = data;
                vm.labels = data[0].labels;
                vm.series = data[0].series;
                vm.data[0] = data[0].datas;
                vm.data[1] = data[0].entryStandart;
                vm.data[2] = data[0].temp;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
}


      
    }
})();
