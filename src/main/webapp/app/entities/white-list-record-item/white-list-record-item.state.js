(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('white-list-record-item', {
            parent: 'entity',
            url: '/white-list-record-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.whiteListRecordItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/white-list-record-item/white-list-record-items.html',
                    controller: 'WhiteListRecordItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('whiteListRecordItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('white-list-record-item-detail', {
            parent: 'white-list-record-item',
            url: '/white-list-record-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.whiteListRecordItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/white-list-record-item/white-list-record-item-detail.html',
                    controller: 'WhiteListRecordItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('whiteListRecordItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WhiteListRecordItem', function($stateParams, WhiteListRecordItem) {
                    return WhiteListRecordItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'white-list-record-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('white-list-record-item-detail.edit', {
            parent: 'white-list-record-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-record-item/white-list-record-item-dialog.html',
                    controller: 'WhiteListRecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WhiteListRecordItem', function(WhiteListRecordItem) {
                            return WhiteListRecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('white-list-record-item.new', {
            parent: 'white-list-record-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-record-item/white-list-record-item-dialog.html',
                    controller: 'WhiteListRecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('white-list-record-item', null, { reload: 'white-list-record-item' });
                }, function() {
                    $state.go('white-list-record-item');
                });
            }]
        })
        .state('white-list-record-item.edit', {
            parent: 'white-list-record-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-record-item/white-list-record-item-dialog.html',
                    controller: 'WhiteListRecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WhiteListRecordItem', function(WhiteListRecordItem) {
                            return WhiteListRecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('white-list-record-item', null, { reload: 'white-list-record-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('white-list-record-item.delete', {
            parent: 'white-list-record-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/white-list-record-item/white-list-record-item-delete-dialog.html',
                    controller: 'WhiteListRecordItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WhiteListRecordItem', function(WhiteListRecordItem) {
                            return WhiteListRecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('white-list-record-item', null, { reload: 'white-list-record-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
