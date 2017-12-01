(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlStringController', QlStringController)
        ;

    QlStringController.$inject = ['$scope', '$state', 'QlString', 'QlStringSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function QlStringController ($scope, $state, QlString, QlStringSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchModel = pagingParams.searchModel;
        vm.currentSearchModel = pagingParams.searchModel;

        loadAll();

        function loadAll () {
            // if (pagingParams.search) {
                QlString.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    active: true,
                    id: pagingParams.searchModel.id,
                    value: pagingParams.searchModel.value,
                    context: pagingParams.searchModel.context
                }, onSuccess, onError);
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
                vm.qlStrings = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                id: vm.searchModel.id,
                value: vm.searchModel.value,
                context: vm.searchModel.context
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.transition();
        }

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearchModel = {};
            vm.transition();
        }
    }
})();
