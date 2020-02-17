'use strict';

describe('Controller Tests', function() {

    describe('BlackListRecordItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBlackListRecordItem, MockRecord, MockBlackListPerson;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBlackListRecordItem = jasmine.createSpy('MockBlackListRecordItem');
            MockRecord = jasmine.createSpy('MockRecord');
            MockBlackListPerson = jasmine.createSpy('MockBlackListPerson');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BlackListRecordItem': MockBlackListRecordItem,
                'Record': MockRecord,
                'BlackListPerson': MockBlackListPerson
            };
            createController = function() {
                $injector.get('$controller')("BlackListRecordItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'facetrackerApp:blackListRecordItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
