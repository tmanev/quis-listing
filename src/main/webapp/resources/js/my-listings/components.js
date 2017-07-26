var MyListingsComponent = {
    init: function (commonVar) {
        // define the item component
        Vue.component('item', {
            template: '#item-template',
            props: {
                model: Object
            },
            data: function () {
                return {
                    open: false
                }
            },
            computed: {
                isFolder: function () {
                    return this.model.children &&
                        this.model.children.length
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
            },
            methods: {
                selectCategory: function () {
                    if (!(this.model.children && this.model.children.length)) {
                        commonVar.addListingApp.$emit('id-selected', this.model);
                    }
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