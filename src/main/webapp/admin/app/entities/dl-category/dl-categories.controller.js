(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('DlCategoriesController', DlCategoriesController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i<total; i++)
                    input.push(i);
                return input;
            };
        });

    DlCategoriesController.$inject = ['$scope', '$state', 'DlCategory', 'DlCategorySearch', 'ParseLinks', 'AlertService',
        'paginationConstants', 'pagingParams', 'TreeUtils', 'DataLanguageHub'];

    function DlCategoriesController ($scope, $state, DlCategory, DlCategorySearch, ParseLinks, AlertService,
                                   paginationConstants, pagingParams, TreeUtils, DataLanguageHub) {
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
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.onLanguageChange = onLanguageChange;
        vm.hasTranslation = hasTranslation;

        vm.selectedLanguageCode = vm.dataLanguageHub.selectedLanguageCode;
        vm.activeLanguages = [
               {code: "en", englishName: "English", count: 0}
        ];

        loadActiveLanguages();
        loadAll();

        function loadActiveLanguages() {
            DlCategory.activeLanguages({

            }, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.activeLanguages = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll () {
            if (pagingParams.search) {
                DlCategorySearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                DlCategory.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    languageCode: vm.selectedLanguageCode
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
                vm.dlCategories = data;
                vm.dlCategoriesTree = TreeUtils.getTree(data, "id", "parentId");
                vm.dlCategoriesFlat = TreeUtils.getFlat(vm.dlCategoriesTree);
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
                search: vm.currentSearch
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

        function hasTranslation(activeLanguage, translations) {
            for (let i = 0; i < translations.length; i++) {
                if (activeLanguage.code === translations[i].languageCode) {
                    return translations[i].id;
                }
            }
            return false;
        }
    }
})();
