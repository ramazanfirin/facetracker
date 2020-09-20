(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .controller('RecordSenseRapor4Controller', RecordSenseRapor4Controller);

    RecordSenseRapor4Controller.$inject = ['$state', 'DataUtils', 'RecordSense', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Person'];

    function RecordSenseRapor4Controller($state, DataUtils, RecordSense, ParseLinks, AlertService, paginationConstants, pagingParams,Person) {

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

        vm.chartHigh = Highcharts.chart('container', {
            title: {
              text: 'Mesai Başlangıç Çizelgesi'
            },
            time: {
                timezoneOffset: -120
              },
//            xAxis: {
//              categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
//                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
//              ]
//            }
//              ,
            
            yAxis: {
              	type: 'datetime'
              	,
              	title :{text:"Mesai Başlangıç Saati"},
                labels: {
                  format: '{value:%H:%M}',
                }
            },   
            tooltip: {
              	formatter: function () {
                	const date = new Date(this.y)
                	const M = date.getMinutes()
                  const H = date.getHours()
                  return `${this.series.name}: ${H}:${M}`
                }
              },
            
            series: [
            	{name: '',
              data: [
////           	  1599629005000
////           	  ,1599716347000
////           	  ,1599808056000
////         	  ,1600063029000
////           	  ,1600148337000
////           	  ,1600239127000
////           	  ,1600322001000
////           	  ,1600406810000
//            	  
//            	  Date.parse('2016-10-20 05:23'), 
//            	  Date.parse('2016-10-20 04:14'), 
//            	  Date.parse('2016-10-20 05:14'), 
//            	  Date.parse('2016-10-20 06:14'),
//            	  Date.parse('2016-10-20 07:14'),
//            	  Date.parse('2016-10-20 08:14'),
////            	  Date.parse('2016-10-20 09:14'),
////            	  Date.parse('2016-10-20 10:14'), 
////            	  Date.parse('2016-10-20 11:14'), 
////            	  Date.parse('2016-10-20 12:14'),
////            	  Date.parse('2016-10-20 13:14'), 
////            	  Date.parse('2016-10-21 14:14')
              ]
               }
            ]
          });
        
        
        
        
        
        
        
        var d = new Date();
        d.setHours(0);
        d.setDate(1);
        
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
                vm.chartHigh.xAxis[0].categories = data[0].labels;
                //vm.chartHigh.series[0].name = data[0].series[0];
                //vm.chartHigh.series[0].data = data[0].entryList;
                //vm.chartHigh.series[1].name = data[0].series[1];
                //vm.chartHigh.series[1].data = data[0].entryStandart;
                
                var series = [];
                var series1= new Object();
                series1.name = data[0].series[0];; 
                //series1.data = data[0].entryList;;
                series1.data = [];
                for (var i=0;i<data[0].entryList.length;i++)
                {
                	var tempHour =	Date.parse(data[0].entryList[i]);
                	tempHour = tempHour+3600000;
                series1.data.push(tempHour);
                }
                
                var series2 =new Object() ;
                series2.name = data[0].series[1];
                //series2.data = data[0].entryStandart
                series2.data = [];
                for (var i=0;i<data[0].entryStandartInstant.length;i++)
                {
                	var tempHour =	Date.parse(data[0].entryStandartInstant[i]);
                	tempHour = tempHour+3600000;
                series2.data.push(tempHour);
                }
                
                
                series.push(series1);
                series.push(series2);
                
                vm.chartHigh.update({
                    series: series
                  }, true, true);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
}


      
    }
})();
