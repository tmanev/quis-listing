var CategoryItemComponent = {
    init: function () {
        // define the item component
        Vue.component('category-item-component', {
            template: '#category-item-component',
            props: {
                category: Object,
                selectedCategory: Object,
                listing: Object
            },
            data: function () {
                return {
                    listing: this.listing,
                    selectedCategory: this.selectedCategory,
                    open: false
                }
            },
            computed: {
                isFolder: function () {
                    return this.category.children && this.category.children.length
                },
                getPanelId: function () {
                    return 'category-panel-' + this._uid
                },
                getPanelIdWithHashTag: function () {
                    return '#category-panel-' + this._uid
                },
                getHeadingId: function () {
                    return 'category-heading-' + this._uid
                },
                getBodyId: function () {
                    return 'category-body-' + this._uid
                },
                getBodyIdWithHashTag: function () {
                    return '#category-body-' + this._uid
                }
            }, methods: {
                selectCategory: function () {
                    if (!(this.category.children && this.category.children.length)) {
                        if (this.selectedCategory.model) {
                            this.selectedCategory.model.active = false;
                        }
                        this.selectedCategory.model = this.category;
                        this.selectedCategory.model.active = true;
                        this.selectedCategory.name = this.category.name;
                        this.listing.dlCategories = [this.selectedCategory.model];
                    }
                },
                toggleCategory: function () {
                    this.open = !this.open;
                }
            },
            created: function () {
                if (this.listing.dlCategories && this.listing.dlCategories.length > 0) {
                    if (this.listing.dlCategories[0].id === this.category.id) {
                        this.selectedCategory.model = this.category;
                        this.selectedCategory.name = this.category.name;
                        this.selectedCategory.model.active = true;
                    }
                }
            }
        });
    }
};