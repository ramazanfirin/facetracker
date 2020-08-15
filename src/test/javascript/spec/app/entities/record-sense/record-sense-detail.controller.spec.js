'use strict';

describe('Controller Tests', function() {

    describe('RecordSense Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRecordSense, MockDevice, MockImage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRecordSense = jasmine.createSpy('MockRecordSense');
            MockDevice = jasmine.createSpy('MockDevice');
            MockImage = jasmine.createSpy('MockImage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'RecordSense': MockRecordSense,
                'Device': MockDevice,
                'Image': MockImage
            };
            createController = function() {
                $injector.get('$controller')("RecordSenseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'facetrackerApp:recordSenseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
