(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('black-list-record-item', {
            parent: 'entity',
            url: '/black-list-record-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.blackListRecordItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/black-list-record-item/black-list-record-items.html',
                    controller: 'BlackListRecordItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blackListRecordItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('black-list-record-item-detail', {
            parent: 'black-list-record-item',
            url: '/black-list-record-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.blackListRecordItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/black-list-record-item/black-list-record-item-detail.html',
                    controller: 'BlackListRecordItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blackListRecordItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BlackListRecordItem', function($stateParams, BlackListRecordItem) {
                    return BlackListRecordItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'black-list-record-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('black-list-record-item-detail.edit', {
            parent: 'black-list-record-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-record-item/black-list-record-item-dialog.html',
                    controller: 'BlackListRecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlackListRecordItem', function(BlackListRecordItem) {
                            return BlackListRecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('black-list-record-item.new', {
            parent: 'black-list-record-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-record-item/black-list-record-item-dialog.html',
                    controller: 'BlackListRecordItemDialogController',
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
                    $state.go('black-list-record-item', null, { reload: 'black-list-record-item' });
                }, function() {
                    $state.go('black-list-record-item');
                });
            }]
        })
        .state('black-list-record-item.edit', {
            parent: 'black-list-record-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-record-item/black-list-record-item-dialog.html',
                    controller: 'BlackListRecordItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BlackListRecordItem', function(BlackListRecordItem) {
                            return BlackListRecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('black-list-record-item', null, { reload: 'black-list-record-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('black-list-record-item.delete', {
            parent: 'black-list-record-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/black-list-record-item/black-list-record-item-delete-dialog.html',
                    controller: 'BlackListRecordItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BlackListRecordItem', function(BlackListRecordItem) {
                            return BlackListRecordItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('black-list-record-item', null, { reload: 'black-list-record-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
