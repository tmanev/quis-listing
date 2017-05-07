(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .controller('QlStringTranslateController', QlStringTranslateController)
        ;

    QlStringTranslateController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Language', 'AlertService'];

    function QlStringTranslateController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Language, AlertService) {
        var vm = this;

        vm.qlString = entity;
        vm.clear = clear;
        vm.save = save;
        vm.loadActiveLanguages = loadActiveLanguages;

        loadActiveLanguages();

        function loadActiveLanguages() {
            Language.query({
                active: true
            }, onSuccess, onError);

            function onSuccess(data) {
                vm.languages = data;

                for (var i = 0; i<vm.languages.length; i++) {
                    addLanguageTranslationIfNeeded(vm.languages[i]);
                }
            }

            function addLanguageTranslationIfNeeded(language) {
                if (!vm.qlString.stringTranslation) {
                    vm.qlString.stringTranslation = [];
                }

                if (!languageExist(vm.qlString.stringTranslation, language)) {
                    var translation = {
                        languageCode: language.code,
                        englishName: language.englishName,
                        value: ""
                    };
                    vm.qlString.stringTranslation.push(translation);
                }
            }

            function languageExist(stringTranslation, languageCode) {
                for (var i = 0; i<stringTranslation.length; i++) {
                    if (stringTranslation[i].languageCode == languageCode) {
                        return true;
                    }
                }
                return false;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

//        function save () {
//            vm.isSaving = true;
//            if (vm.language.id !== null) {
//                Language.update(vm.language, onSaveSuccess, onSaveError);
//            } else {
//                Language.save(vm.language, onSaveSuccess, onSaveError);
//            }
//        }

         function save () {
                    vm.isSaving = true;
                    if (vm.qlString.id !== null) {
                        QlString.update(vm.qlString, onSaveSuccess, onSaveError);
                    } else {
                        QlString.save(vm.qlString, onSaveSuccess, onSaveError);
                    }
                }

        function onSaveSuccess (result) {
            $scope.$emit('quisListingApp:QlStringUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
