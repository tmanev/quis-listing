var ListingDetailsComponent = {
  init: function () {
      Vue.use(window.vuelidate.default);
      const {required, minLength, maxLength, between, email, sameAs} = window.validators;

      Vue.component('listing-details-component', {
          template: '#listing-details-component',
          props: {
              dlContentFields: Object
          },
          data: function() {
              return this.dlContentFields;
          },
          validations: function () {
              let validation_dict = {};

              validation_dict.dlContentFields = {};

              for (let index = 0; index < this.dlContentFields.length; index++) {
                  validation_dict.dlContentFields[index] = {};
                  validation_dict.dlContentFields[index].value = {};
                  validation_dict.dlContentFields[index].selectedValue = {};
                  if (this.dlContentFields[index].required) {
                      if (this.dlContentFields[index].type === 'SELECT' || this.dlContentFields[index].type === 'DEPENDENT_SELECT') {
                          validation_dict.dlContentFields[index].selectedValue.ListHasSelectionValidator = listHasSelectionValidator;
                      } else if(this.dlContentFields[index].type === 'NUMBER_UNIT') {
                          validation_dict.dlContentFields[index].selectedValue.ListHasSelectionValidator = listHasSelectionValidator;
                          validation_dict.dlContentFields[index].value.required = required;
                      } else {
                          validation_dict.dlContentFields[index].value.required = required;
                      }
                  }

                  if (this.dlContentFields[index].type === 'NUMBER' || this.dlContentFields[index].type === 'NUMBER_UNIT') {
                      let min = Number.MIN_SAFE_INTEGER;
                      let max = Number.MAX_SAFE_INTEGER;

                      if (this.dlContentFields[index].optionsModel.min && this.dlContentFields[index].optionsModel.min !== '') {
                          min = this.dlContentFields[index].optionsModel.min;
                      }

                      if (this.dlContentFields[index].optionsModel.max && this.dlContentFields[index].optionsModel.max !== '') {
                          max = this.dlContentFields[index].optionsModel.max;
                      }

                      validation_dict.dlContentFields[index].value.between = between(min, max);
                  }

                  if (this.dlContentFields[index].type === 'STRING') {
                      if (this.dlContentFields[index].optionsModel.minLength && this.dlContentFields[index].optionsModel.minLength !== '') {
                          validation_dict.dlContentFields[index].value.minLength = minLength(this.dlContentFields[index].optionsModel.minLength);
                      } else {
                          validation_dict.dlContentFields[index].value.minLength = minLength(0);
                      }

                      if (this.dlContentFields[index].optionsModel.maxLength && this.dlContentFields[index].optionsModel.maxLength !== '') {
                          validation_dict.dlContentFields[index].value.maxLength = maxLength(this.dlContentFields[index].optionsModel.maxLength);
                      } else {
                          validation_dict.dlContentFields[index].value.maxLength = maxLength(4096);
                      }
                  }
              }

              return validation_dict;
          },
          methods: {
              isInputInvalid: function () {
                  if (this.$v.$invalid) {
                      this.$v.$touch();
                      return false;
                  }
                  return true;
              },
              rootContentFieldItem: function(dlContentFieldItems) {
                  return dlContentFieldItems.filter(function (dlContentFieldItem) {
                      return dlContentFieldItem.parent === null;
                  });
              },
              childContentFieldItem: function(dlContentFieldItems, parentId) {
                  return dlContentFieldItems.filter(function (dlContentFieldItem) {
                      return dlContentFieldItem.parent !== null && dlContentFieldItem.parent.id === parentId;
                  });
              },
              resetValue: function(dlContentField) {
                  dlContentField.value = -1;
              },
              getListingFields: function () {
                  let dlListingFields = [];
                  for (let dlContentField of this.dlContentFields) {
                      let value;
                      let selectedValue;
                      if (dlContentField.type === 'CHECKBOX') {
                          if (dlContentField.selectedValue) {
                              selectedValue = JSON.stringify(dlContentField.selectedValue);
                          } else {
                              selectedValue = JSON.stringify([]);
                          }
                      } else if (dlContentField.type === 'SELECT' || dlContentField.type === 'DEPENDENT_SELECT') {
                          selectedValue = dlContentField.selectedValue;
                      } else if (dlContentField.type === 'NUMBER_UNIT') {
                          selectedValue = dlContentField.selectedValue;
                          value = dlContentField.value;
                      } else {
                          value = dlContentField.value;
                      }
                      let listingField = {
                          id: dlContentField.id,
                          value: value,
                          selectedValue: selectedValue
                      };
                      dlListingFields.push(listingField);
                  }
                  return dlListingFields;
              }
          }
      });
  },
    setContentFieldValuesFromDlListing: function (dlListingDTO, dlContentFieldsDto) {
        for (let i = 0; i < dlContentFieldsDto.length; i++) {
            let dlContentField = dlContentFieldsDto[i];

            if (dlContentField.options) {
                dlContentField.optionsModel = JSON.parse(dlContentField.options);
            }

            getValueFromContentField(dlContentField, dlListingDTO.dlListingFields);
        }

        function getValueFromContentField(dlContentField, dlListingFields) {

            if (dlListingFields) {
                for (let dlListingField of dlListingFields) {
                    if (dlListingField.id === dlContentField.id) {
                        if (dlContentField.type === 'CHECKBOX') {
                            if (dlListingField.selectedValue) {
                                dlContentField.selectedValue = JSON.parse(dlListingField.selectedValue);
                            } else {
                                dlContentField.selectedValue = [];
                            }
                            return;
                        } else if (dlContentField.type === 'DEPENDENT_SELECT') {
                            if (dlListingField.selectedValue) {
                                dlContentField.parentValue = findDlContentFieldItem(dlListingField.selectedValue, dlContentField.dlContentFieldItems);
                                dlContentField.selectedValue = dlListingField.selectedValue;
                            } else {
                                dlContentField.parentValue = -1;
                                dlContentField.selectedValue = -1;
                            }
                            return;
                        } else if (dlContentField.type === 'SELECT') {
                            if (dlListingField.selectedValue) {
                                dlContentField.selectedValue = dlListingField.selectedValue;
                            } else {
                                dlContentField.selectedValue = -1;
                            }
                            return;
                        } else if (dlContentField.type === 'NUMBER_UNIT') {
                            if (dlListingField.selectedValue) {
                                dlContentField.selectedValue = dlListingField.selectedValue;
                            } else {
                                dlContentField.selectedValue = -1;
                            }
                            dlContentField.value = dlListingField.value;
                            return;
                        } else {
                            dlContentField.value = dlListingField.value;
                            return;
                        }
                    }
                }
                // if it gets here means content field is added and there is no relation with the dlListing
                // return default value for the proper type
                return defaultValueIfNoRelation(dlContentField);
            } else {
                return defaultValueIfNoRelation(dlContentField);
            }

            function findDlContentFieldItem(id, dlContentFieldItems) {
                for (let dlContentFieldItem of dlContentFieldItems) {
                    if (dlContentFieldItem.id == id) {
                        return dlContentFieldItem.parent.id;
                    }
                }
            }

            function defaultValueIfNoRelation(dlContentField) {
                if (dlContentField.type === 'CHECKBOX') {
                    // return empty array to be able to operate checkbox
                    dlContentField.selectedValue = [];
                } else if (dlContentField.type === 'SELECT') {
                    dlContentField.selectedValue = -1;
                } else if (dlContentField.type === 'DEPENDENT_SELECT') {
                    dlContentField.parentValue = -1;
                    dlContentField.selectedValue = -1;
                } else if (dlContentField.type === 'NUMBER_UNIT') {
                    dlContentField.selectedValue = -1;
                }
            }
        }
    }
};
