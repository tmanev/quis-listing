(function() {
    'use strict';

    angular
        .module('quisListingApp')
        .factory('TreeUtils', TreeUtils);

    function TreeUtils () {
        var service = {
            getTree : getTree,
            getFlat : getFlat
        };

        return service;

        function getTree(data, primaryIdName, parentIdName) {
            if (!data || data.length == 0 || !primaryIdName || !parentIdName)
                return [];

            var tree = [],
                rootIds = [],
                item = data[0],
                primaryKey = item[primaryIdName],
                treeObjs = {},
                parentId,
                parent,
                len = data.length,
                i = 0;

            while (i < len) {
                item = data[i++];
                primaryKey = item[primaryIdName];
                treeObjs[primaryKey] = item;
                parentId = item[parentIdName];

                if (parentId) {
                    parent = treeObjs[parentId];

                    if (parent.children) {
                        parent.children.push(item);
                    } else {
                        parent.children = [item];
                    }
                } else {
                    rootIds.push(primaryKey);
                }
            }

            for (var i = 0; i < rootIds.length; i++) {
                tree.push(treeObjs[rootIds[i]]);
            }

            return tree;
        }

        function getFlat(tree) {
            var flatObjs = [];
            for (var i = 0, len = tree.length; i< len; i++) {
                getChildren(tree[i], flatObjs);
            }

            return flatObjs;
        }

        function getChildren(data, $array) {
            $array.push(data);
            var children = data.children;
            if (children){
                for (var i = 0, len = children.length; i < len; i++) {
                    getChildren(children[i], $array);
                }
            }
        }
    }

})();