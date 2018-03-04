var listingCategoryComponent = ListingCategoryComponent = {
    init: function (dlCategoriesDtoFlat) {
        Vue.use(window.vuelidate.default);
        const {required, minLength, maxLength, between, email, sameAs} = window.validators;
        var roots = ListingCategoryComponent.Utils.flatItemsToTree(dlCategoriesDtoFlat);

        // define the item component
        Vue.component('listing-category-component', {
            template: '#listing-category-component',
            props: {
                listing: Object
            },
            data: function () {
                return {
                    listing: this.listing,
                    categories: roots,
                    selectedCategory: {
                        model: null,
                        name: ''
                    }
                }
            },
            validations: function () {
                return {
                    selectedCategory: {
                        name: {
                            required: required
                        }
                    }
                };
            },
            methods: {
                openCategorySelection: function () {
                    this.$refs.categoryModal.show();
                },
                hideModal: function () {
                    this.$refs.categoryModal.hide();
                },
                isInputInvalid: function () {
                    if (this.$v.$invalid) {
                        this.$v.$touch();
                        return false;
                    }
                    return true;
                }
            }
        });
    },
    Utils: {
        flatItemsToTree: function (nodes) {
            var map = {}, node, roots = [];
            for (var i = 0; i < nodes.length; i += 1) {
                node = nodes[i];
                node.children = [];
                node.active = false;
                map[node.id] = i; // use map to look-up the parents
                if (node.parentId !== null) {
                    nodes[map[node.parentId]].children.push(node);
                } else {
                    roots.push(node);
                }
            }
            return roots;
        }
    }
};