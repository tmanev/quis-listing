(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('NavMenuDialogController', NavMenuDialogController);

    NavMenuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'NavMenu',
        'QlPage', 'AlertService'];

    function NavMenuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, NavMenu,
                                      QlPage, AlertService) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.navMenu = entity;
        vm.clear = clear;
        vm.save = save;

        vm.loadQlPages = loadQlPages;
        vm.addMenuItem = addMenuItem;
        vm.removeMenuItem = removeMenuItem;

        loadQlPages();

        function loadQlPages () {
            QlPage.query({
                languageCode: entity.languageCode
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.qlPages = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function addMenuItem(post) {
            if (!vm.navMenu.navMenuItemDTOs){
                vm.navMenu.navMenuItemDTOs = [];
            }
            var navMenuItem = {};
            navMenuItem.id = null;
            navMenuItem.title = post.title;
            navMenuItem.name = '';
            navMenuItem.order = vm.navMenu.navMenuItemDTOs.length + 1;
            navMenuItem.refItem = {};
            navMenuItem.refItem.id = post.id;
            navMenuItem.refItem.title = post.title;
            navMenuItem.refItem.name = post.name;

            vm.navMenu.navMenuItemDTOs.push(navMenuItem);
        }

        function removeMenuItem(menuItem) {
            var index = vm.navMenu.navMenuItemDTOs.indexOf(menuItem);
            if (index > -1) {
                vm.navMenu.navMenuItemDTOs.splice(index, 1);
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.navMenu.id !== null) {
                NavMenu.update(vm.navMenu, onSaveSuccess, onSaveError);
            } else {
                NavMenu.save(vm.navMenu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:navMenuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
