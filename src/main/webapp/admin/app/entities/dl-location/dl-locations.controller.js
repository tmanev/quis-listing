(function () {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlLocationsController', DlLocationsController)
        .filter('range', function () {
            return function (input, total) {
                total = parseInt(total);
                for (var i = 0; i < total; i++)
                    input.push(i);
                return input;
            };
        });

    DlLocationsController.$inject = ['$scope', '$state', 'DlLocation', 'DlLocationSearch', 'ParseLinks', 'AlertService',
        'paginationConstants', 'pagingParams', 'dlLocationParentId', 'TreeUtils', 'DataLanguageHub'];

    function DlLocationsController($scope, $state, DlLocation, DlLocationSearch, ParseLinks, AlertService,
                                   paginationConstants, pagingParams, dlLocationParentId, TreeUtils, DataLanguageHub) {
        var vm = this;
        vm.dataLanguageHub = DataLanguageHub.get();
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.loadParentFilter = loadParentFilter;
        vm.loadTable = loadTable;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.onLanguageChange = onLanguageChange;
        vm.clearParentLocationFilter = clearParentLocationFilter;

        vm.filter = {            dlLocationParentId: dlLocationParentId        };

        vm.onSelectCallback = function ($item, $model) {
            console.log("Item:");
            console.log($item);
            console.log("Model:");
            console.log($model);
            vm.loadTable();
        };

        vm.selectedLanguageCode = vm.dataLanguageHub.selectedLanguageCode;
        vm.activeLanguages = [
            {code: "en", englishName: "English", count: 0}
        ];

        loadActiveLanguages();
        loadAll();

        function loadActiveLanguages() {
            DlLocation.activeLanguages({}, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.activeLanguages = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadParentFilter() {
            DlLocation.query({
                sort: sort(),
                languageCode: vm.selectedLanguageCode
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
                // return ['id'];
            }

            function onSuccess(data) {
                var dlLocationsTree = TreeUtils.getTree(data, "id", "parentId");
                vm.dlLocationsFilter = TreeUtils.getFlat(dlLocationsTree);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadTable() {
            if (pagingParams.search) {
                DlLocationSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                DlLocation.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    languageCode: vm.selectedLanguageCode,
                    parentId: vm.filter.dlLocationParentId
                }, onSuccess, onError);
            }
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
                vm.dlLocations = data;
                vm.dlLocationsTree = TreeUtils.getTree(data, "id", "parentId");
                vm.dlLocationsFlat = TreeUtils.getFlat(vm.dlLocationsTree);
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            vm.loadParentFilter();
            vm.loadTable();
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

        function search(searchQuery) {
            if (!searchQuery) {
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }

        function onLanguageChange() {
            loadAll();
            vm.dataLanguageHub.selectedLanguageCode = vm.selectedLanguageCode;
        }

        function clearParentLocationFilter() {
            vm.filter.dlLocationParentId = null;
            vm.loadTable();
        }
    }
})();
