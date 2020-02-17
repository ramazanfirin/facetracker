(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('black-list-person', {
            parent: 'entity',
            url: '/black-list-person',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.blackListPerson.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/black-list-person/black-list-people.html',
                    controller: 'BlackListPersonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blackListPerson');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('black-list-person-detail', {
            parent: 'black-list-person',
            url: '/black-list-person/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.blackListPerson.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/black-list-person/black-list-person-detail.html',
                    controller: 'BlackListPersonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blackListPerson');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BlackListPerson', function($stateParams, BlackListPerson) {
                    return BlackListPerson.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'black-list-person',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('black-list-person-detail.edit', {
            parent: 'black-list-person-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-person/black-list-person-dialog.html',
                    controller: 'BlackListPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlackListPerson', function(BlackListPerson) {
                            return BlackListPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('black-list-person.new', {
            parent: 'black-list-person',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-person/black-list-person-dialog.html',
                    controller: 'BlackListPersonDialogController',
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
                    $state.go('black-list-person', null, { reload: 'black-list-person' });
                }, function() {
                    $state.go('black-list-person');
                });
            }]
        })
        .state('black-list-person.edit', {
            parent: 'black-list-person',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-person/black-list-person-dialog.html',
                    controller: 'BlackListPersonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlackListPerson', function(BlackListPerson) {
                            return BlackListPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('black-list-person', null, { reload: 'black-list-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('black-list-person.delete', {
            parent: 'black-list-person',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-person/black-list-person-delete-dialog.html',
                    controller: 'BlackListPersonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BlackListPerson', function(BlackListPerson) {
                            return BlackListPerson.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('black-list-person', null, { reload: 'black-list-person' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
