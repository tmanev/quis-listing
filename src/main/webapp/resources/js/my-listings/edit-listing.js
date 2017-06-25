EditListing = {
    init: function () {
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

        var nodes = JSON.parse('[{"id":1,"term":{"id":1,"name":"Work","slug":"work"},"parentId":null,"description":"Work Description","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":2,"term":{"id":2,"name":"Real Estate - Sales","slug":"real-estate-sales"},"parentId":null,"description":"Sales of Real Estate","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":3,"term":{"id":3,"name":"Real Estate - Rents","slug":"real-estate-rents"},"parentId":null,"description":"Rents of Real Estate","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":4,"term":{"id":4,"name":"Auto - Moto","slug":"auto-moto"},"parentId":null,"description":"Auto Moto","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":5,"term":{"id":5,"name":"Automobiles, Jeeps, Trucks","slug":"automobiles-jeeps-trucks"},"parentId":4,"description":"Automobiles, Jeeps, Pickups any car","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":1},{"id":6,"term":{"id":6,"name":"Trucks","slug":"trucks"},"parentId":4,"description":"Trucks for you","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":1},{"id":1,"term":{"id":1,"name":"Work","slug":"work"},"parentId":null,"description":"Work Description","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":2,"term":{"id":2,"name":"Real Estate - Sales","slug":"real-estate-sales"},"parentId":null,"description":"Sales of Real Estate","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":3,"term":{"id":3,"name":"Real Estate - Rents","slug":"real-estate-rents"},"parentId":null,"description":"Rents of Real Estate","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":4,"term":{"id":4,"name":"Auto - Moto","slug":"auto-moto"},"parentId":null,"description":"Auto Moto","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":0},{"id":5,"term":{"id":5,"name":"Automobiles, Jeeps, Trucks","slug":"automobiles-jeeps-trucks"},"parentId":4,"description":"Automobiles, Jeeps, Pickups any car","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":1},{"id":6,"term":{"id":6,"name":"Trucks","slug":"trucks"},"parentId":4,"description":"Trucks for you","count":0,"languageCode":"en","sourceLanguageCode":null,"translationGroupId":null,"translations":null,"depthLevel":1}]');

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
        console.log(roots);

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
                        addListingApp.$emit('id-selected', this.model);
                    }
                }
            }
        });

        var editListingApp = new Vue({
            el: '#editListingApp',
            data: {
                categories: roots,
                listing: {

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