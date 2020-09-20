(function() {
    'use strict';

    angular
        .module('facetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('record-sense', {
            parent: 'entity',
            url: '/record-sense?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.recordSense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-sense/record-senses.html',
                    controller: 'RecordSenseController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordSense');
                    $translatePartialLoader.addPart('recordStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('record-sense-rapor', {
            parent: 'entity',
            url: '/record-sense-rapor?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.recordSense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-sense/record-senses-rapor.html',
                    controller: 'RecordSenseRaporController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordSense');
                    $translatePartialLoader.addPart('recordStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('record-sense-rapor-2', {
            parent: 'entity',
            url: '/record-sense-rapor-2?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.recordSense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-sense/record-senses-rapor2.html',
                    controller: 'RecordSenseRapor2Controller',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordSense');
                    $translatePartialLoader.addPart('recordStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('record-sense-rapor-3', {
            parent: 'entity',
            url: '/record-sense-rapor-3?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.recordSense.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-sense/record-senses-rapor3.html',
                    controller: 'RecordSenseRapor3Controller',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordSense');
                    $translatePartialLoader.addPart('recordStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('record-sense-detail', {
            parent: 'record-sense',
            url: '/record-sense/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'facetrackerApp.recordSense.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/record-sense/record-sense-detail.html',
                    controller: 'RecordSenseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('recordSense');
                    $translatePartialLoader.addPart('recordStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'RecordSense', function($stateParams, RecordSense) {
                    return RecordSense.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'record-sense',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('record-sense-detail.edit', {
            parent: 'record-sense-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-sense/record-sense-dialog.html',
                    controller: 'RecordSenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RecordSense', function(RecordSense) {
                            return RecordSense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('record-sense.new', {
            parent: 'record-sense',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-sense/record-sense-dialog.html',
                    controller: 'RecordSenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                insert: null,
                                path: null,
                                fileSentDate: null,
                                fileCreationDate: null,
                                processStartDate: null,
                                processFinishDate: null,
                                status: null,
                                afid: null,
                                afidContentType: null,
                                isProcessed: null,
                                similarity: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('record-sense', null, { reload: 'record-sense' });
                }, function() {
                    $state.go('record-sense');
                });
            }]
        })
        .state('record-sense.edit', {
            parent: 'record-sense',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-sense/record-sense-dialog.html',
                    controller: 'RecordSenseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['RecordSense', function(RecordSense) {
                            return RecordSense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('record-sense', null, { reload: 'record-sense' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('record-sense.delete', {
            parent: 'record-sense',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/record-sense/record-sense-delete-dialog.html',
                    controller: 'RecordSenseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['RecordSense', function(RecordSense) {
                            return RecordSense.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('record-sense', null, { reload: 'record-sense' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
