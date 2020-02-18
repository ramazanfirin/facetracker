(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('floor', {
            parent: 'entity',
            url: '/floor',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.floor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/floor/floors.html',
                    controller: 'FloorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('floor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('floor-detail', {
            parent: 'floor',
            url: '/floor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.floor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/floor/floor-detail.html',
                    controller: 'FloorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('floor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Floor', function($stateParams, Floor) {
                    return Floor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'floor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('floor-detail.edit', {
            parent: 'floor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-dialog.html',
                    controller: 'FloorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Floor', function(Floor) {
                            return Floor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('floor.new', {
            parent: 'floor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-dialog.html',
                    controller: 'FloorDialogController',
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
                    $state.go('floor', null, { reload: 'floor' });
                }, function() {
                    $state.go('floor');
                });
            }]
        })
        .state('floor.edit', {
            parent: 'floor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-dialog.html',
                    controller: 'FloorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Floor', function(Floor) {
                            return Floor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('floor', null, { reload: 'floor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('floor.delete', {
            parent: 'floor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/floor/floor-delete-dialog.html',
                    controller: 'FloorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Floor', function(Floor) {
                            return Floor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('floor', null, { reload: 'floor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
