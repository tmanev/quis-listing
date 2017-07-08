AddListing = {
    init: function (dlCategoriesDtoFlat, commonVar) {
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

        var addListingApp = new Vue({
            el: '#addListingApp',
            data: {
                categories: roots,
                listing: {
                    title: '',
                    selectedCategory: null,
                    category: {
                        id: null,
                        name: ''
                    }
                }
            },
            validations: {
                listing: {
                    title: {
                        required: required,
                        TwoWordValidator: TwoWordValidator
                    },
                    category: {
                        name: {
                            required: required
                        }
                    }
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
                onSubmit: function (event) {
                    if (this.$v.listing.$invalid) {
                        this.$v.listing.$touch();
                    } else {
                        var payload = {};
                        payload.title = this.listing.title;
                        payload.dlCategories = [this.listing.selectedCategory];
                        var $btn = $('#btnNext').button('loading');
                        this.$http({
                            url: '/api/dl-listings',
                            body: payload,
                            method: 'POST'
                        }).then(function (response) {
                            console.log('Success!:', response.data);
                            $btn.button('reset');

                            // move the user to the next page
                            let editUrl = '/my-listings/edit/' + response.data.id;
                            window.location.href = editUrl;

                        }, function (response) {
                            console.log('Error!:', response.data);
                            $.notify({
                                message: response.data
                            }, {
                                type: 'danger'
                            });
                            $btn.button('reset');
                        });
                    }
                },
                openCategorySelection: function ($v) {
                    // $v.$touch();
                    this.delayTouch($v);
                    $('#myModal').modal('toggle');
                }
            }
        });

        // var bus = new Vue();

        // commonVar.bus = new Vue();
        commonVar.addListingApp = addListingApp;

        addListingApp.$on('id-selected', function (category) {
            if (this.listing.selectedCategory) {
                this.listing.selectedCategory.active = false;
            }
            this.listing.selectedCategory = category;
            this.listing.selectedCategory.active = true;
            this.listing.category.id = category.id;
            this.listing.category.name = category.term.name;
        });
    }
};