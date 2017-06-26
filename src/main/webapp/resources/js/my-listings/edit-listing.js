EditListing = {
    init: function (dlListingDTO, dlCategoriesDtoFlat, commonVar) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;

        /**
         * @return {boolean}
         */
        const TwoWordValidator = function (value, component) {
            var s = value;
            s = s.replace(/(^\s*)|(\s*$)/gi, "");
            s = s.replace(/[ ]{2,}/gi, " ");
            s = s.replace(/\n /, "\n");
            return s.split(' ').length >= 2;
        };

        const touchMap = new WeakMap();

        var roots = MyListingsComponent.Utils.flatItemsToTree(dlCategoriesDtoFlat);

        var selectedCategory = {
            term: {
                name: ''
            }
        };
        if (dlListingDTO.dlCategories && dlListingDTO.dlCategories.length > 0) {
            selectedCategory = dlListingDTO.dlCategories[0];
        }

        var editListingApp = new Vue({
            el: '#editListingApp',
            data: {
                categories: roots,
                selectedCategory: selectedCategory,
                listing: {
                    id: dlListingDTO.id,
                    title: dlListingDTO.title,
                    content: dlListingDTO.content,
                    name: dlListingDTO.name,
                    status: dlListingDTO.status,
                    dlCategories: dlListingDTO.dlCategories,
                    dlLocations: dlListingDTO.dlLocations,
                    dlListingFields: dlListingDTO.dlListingFields,
                    attachments: dlListingDTO.attachments
                }
            },
            validations: {
                listing: {

                }
            },
            methods: {
                delayTouch: function ($v) {
                    $v.$reset();
                    if (touchMap.has($v)) {
                        clearTimeout(touchMap.get($v));
                    }
                    touchMap.set($v, setTimeout($v.$touch, 1000))
                },
                onSubmit: function (event) {},
                openCategorySelection: function ($v) {
                    // $v.$touch();
                    this.delayTouch($v);
                    $('#myModal').modal('toggle');
                }
            }
        });
    }
};