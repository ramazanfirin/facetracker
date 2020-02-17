'use strict';

describe('Controller Tests', function() {

    describe('WhiteListRecordItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWhiteListRecordItem, MockRecord, MockWhiteListPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWhiteListRecordItem = jasmine.createSpy('MockWhiteListRecordItem');
            MockRecord = jasmine.createSpy('MockRecord');
            MockWhiteListPerson = jasmine.createSpy('MockWhiteListPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WhiteListRecordItem': MockWhiteListRecordItem,
                'Record': MockRecord,
                'WhiteListPerson': MockWhiteListPerson
            };
            createController = function() {
                $injector.get('$controller')("WhiteListRecordItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'facetrackerApp:whiteListRecordItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
