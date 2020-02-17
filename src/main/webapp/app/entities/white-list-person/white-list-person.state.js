(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('white-list-person', {
            parent: 'entity',
            url: '/white-list-person',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.whiteListPerson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/white-list-person/white-list-people.html',
                    controller: 'WhiteListPersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('whiteListPerson');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('white-list-person-detail', {
            parent: 'white-list-person',
            url: '/white-list-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.whiteListPerson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/white-list-person/white-list-person-detail.html',
                    controller: 'WhiteListPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('whiteListPerson');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WhiteListPerson', function($stateParams, WhiteListPerson) {
                    return WhiteListPerson.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'white-list-person',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('white-list-person-detail.edit', {
            parent: 'white-list-person-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-person/white-list-person-dialog.html',
                    controller: 'WhiteListPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WhiteListPerson', function(WhiteListPerson) {
                            return WhiteListPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('white-list-person.new', {
            parent: 'white-list-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-person/white-list-person-dialog.html',
                    controller: 'WhiteListPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('white-list-person', null, { reload: 'white-list-person' });
                }, function() {
                    $state.go('white-list-person');
                });
            }]
        })
        .state('white-list-person.edit', {
            parent: 'white-list-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-person/white-list-person-dialog.html',
                    controller: 'WhiteListPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WhiteListPerson', function(WhiteListPerson) {
                            return WhiteListPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('white-list-person', null, { reload: 'white-list-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('white-list-person.delete', {
            parent: 'white-list-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-person/white-list-person-delete-dialog.html',
                    controller: 'WhiteListPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WhiteListPerson', function(WhiteListPerson) {
                            return WhiteListPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('white-list-person', null, { reload: 'white-list-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
