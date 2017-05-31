(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('EmailTemplateDialogController', EmailTemplateDialogController)
        .filter('range', function() {
            return function(input, total) {
                total = parseInt(total);
                for (var i=0; i < total; i++)
                    input.push(i);
                return input;
            };
        });

    EmailTemplateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity',
        'EmailTemplate', 'AlertService', 'Language'];

    function EmailTemplateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,
                                            EmailTemplate, AlertService, Language) {
        var vm = this;
        vm.predicate = 'id';
        vm.reverse = true;

        vm.emailTemplate = entity;
        vm.clear = clear;
        vm.save = save;

        vm.loadActiveLanguages = loadActiveLanguages;

        // dynamic tabs creation
        vm.activeLanguages = [
            {code: "en", englishName: "English"}
        ];

        vm.tabs = [];

        loadActiveLanguages();

        function loadActiveLanguages() {
            Language.query({
                active: true
            }, onSuccess, onError);
            function onSuccess(data) {
                vm.activeLanguages = data;
                for (let language of vm.activeLanguages) {
                    let text = findTextForLanguage(language.code);
                    vm.tabs.push({
                        title: language.englishName,
                        text: text,
                        languageCode: language.code
                    });
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

            function findTextForLanguage(languageCode) {
                if (vm.emailTemplate.qlString) {
                    if (vm.emailTemplate.qlString.languageCode === languageCode) {
                        return vm.emailTemplate.qlString.value;
                    } else {
                        for (let i = 0; i<vm.emailTemplate.qlString.stringTranslation.length; i++) {
                            if (vm.emailTemplate.qlString.stringTranslation[i].languageCode===languageCode) {
                                return vm.emailTemplate.qlString.stringTranslation[i].value;
                            }
                        }
                    }
                }
                return '';
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

            vm.emailTemplate.text = vm.tabs[0].text;
            vm.emailTemplate.qlString = {};
            vm.emailTemplate.qlString.id = null;
            vm.emailTemplate.qlString.value = vm.tabs[0].text;
            vm.emailTemplate.qlString.languageCode = vm.tabs[0].languageCode;

            // iterate the rest of the tabs and update values
            vm.emailTemplate.qlString.stringTranslation = [];
            for (let i = 1; i < vm.tabs.length; i++) {
                let stringTranslation = {
                    id: null,
                    value: vm.tabs[i].text,
                    languageCode: vm.tabs[i].languageCode
                };
                vm.emailTemplate.qlString.stringTranslation.push(stringTranslation);
            }

            if (vm.emailTemplate.id !== null) {
                EmailTemplate.update(vm.emailTemplate, onSaveSuccess, onSaveError);
            } else {
                EmailTemplate.save(vm.emailTemplate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:emailTemplateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
