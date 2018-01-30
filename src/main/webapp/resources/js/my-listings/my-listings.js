var MyListings = {
    init: function (jsTranslations) {
        Vue.use(window.vuelidate.default);

        function parseLinks(header) {
            if (header.length === 0) {
                throw new Error('input must not be of zero length');
            }

            // Split parts by comma
            var parts = header.split(',');
            var links = {};
            // Parse each part into a named link
            parts.forEach(function(p, index) {
                var section = p.split('>;');
                if (section.length !== 2) {
                    throw new Error('section could not be split on ">;"');
                }
                var url = section[0].replace(/<(.*)/, '$1').trim();
                var queryString = {};
                url.replace(
                    new RegExp('([^?=&]+)(=([^&]*))?', 'g'),
                    function($0, $1, $2, $3) { queryString[$1] = $3; }
                );
                var page = queryString.page;
                if (typeof page === 'string' || page instanceof String) {
                    page = parseInt(page);
                }
                var name = section[1].replace(/rel="(.*)"/, '$1').trim();
                links[name] = page;
            });
            return links;
        }

        // Create page object used in template
        function makePage(number, text, isActive) {
            return {
                number: number,
                text: text,
                active: isActive
            };
        }

        function getPages(currentPage, totalPages) {
            var pages = [];

            var rotate = false;
            var forceEllipses = false;
            var boundaryLinkNumbers = false;

            function pageLabel (idx) {
                return idx;
            }

            var maxSize = 20;

            // Default page limits
            var startPage = 1;
            var endPage = totalPages;
            var isMaxSized = (maxSize !== undefined && maxSize !== null) && maxSize < totalPages;

            // recompute if maxSize
            if (isMaxSized) {
                if (rotate) {
                    // Current page is displayed in the middle of the visible ones
                    startPage = Math.max(currentPage - Math.floor(maxSize / 2), 1);
                    endPage = startPage + maxSize - 1;

                    // Adjust if limit is exceeded
                    if (endPage > totalPages) {
                        endPage = totalPages;
                        startPage = endPage - maxSize + 1;
                    }
                } else {
                    // Visible pages are paginated with maxSize
                    startPage = (Math.ceil(currentPage / maxSize) - 1) * maxSize + 1;

                    // Adjust last page if limit is exceeded
                    endPage = Math.min(startPage + maxSize - 1, totalPages);
                }
            }

            // Add page number links
            for (var number = startPage; number <= endPage; number++) {
                var page = makePage(number, pageLabel(number), number === currentPage);
                pages.push(page);
            }

            // Add links to move between page sets
            if (isMaxSized && maxSize > 0 && (!rotate || forceEllipses || boundaryLinkNumbers)) {
                if (startPage > 1) {
                    if (!boundaryLinkNumbers || startPage > 3) { //need ellipsis for all options unless range is too close to beginning
                        var previousPageSet = makePage(startPage - 1, '...', false);
                        pages.unshift(previousPageSet);
                    }
                    if (boundaryLinkNumbers) {
                        if (startPage === 3) { //need to replace ellipsis when the buttons would be sequential
                            var secondPageLink = makePage(2, '2', false);
                            pages.unshift(secondPageLink);
                        }
                        //add the first page
                        var firstPageLink = makePage(1, '1', false);
                        pages.unshift(firstPageLink);
                    }
                }

                if (endPage < totalPages) {
                    if (!boundaryLinkNumbers || endPage < totalPages - 2) { //need ellipsis for all options unless range is too close to end
                        var nextPageSet = makePage(endPage + 1, '...', false);
                        pages.push(nextPageSet);
                    }
                    if (boundaryLinkNumbers) {
                        if (endPage === totalPages - 2) { //need to replace ellipsis when the buttons would be sequential
                            var secondToLastPageLink = makePage(totalPages - 1, totalPages - 1, false);
                            pages.push(secondToLastPageLink);
                        }
                        //add the last page
                        var lastPageLink = makePage(totalPages, totalPages, false);
                        pages.push(lastPageLink);
                    }
                }
            }
            return pages;
        }

        var myListingsApp = new Vue({
            el: '#myListingsApp',
            filters: {
                fullTime: function(date) {
                    return moment(date).format('MMMM Do YYYY, h:mm');
                }
            },
            data: {
                pages: [],
                pagination: {
                    boundaryLinks: false,
                    boundaryLinkNumbers: false,
                    directionLinks: true,
                    rotate: false,
                    forceEllipses: false
                },
                pagingParams: {
                    page: 1,
                    sort: null,
                    search: null,
                    predicate: null,
                    ascending: null,
                    itemsPerPage: 10,
                    info: {
                        queryCount: 0,
                        totalItems: 0,
                        totalPages: 0
                    }
                },
                isLoading: false,
                dlListings: [],
                confirmModal: {
                    listingToDelete: null
                }
            },
            validations: {},
            methods: {
                calculateTotalPages: function() {
                    var totalPages = this.pagingParams.itemsPerPage < 1 ? 1 : Math.ceil(this.pagingParams.info.totalItems / this.pagingParams.itemsPerPage);
                    return Math.max(totalPages || 0, 1);
                },
                noPrevious: function () {
                    return this.pagingParams.page === 1;
                },
                noNext: function() {
                    return this.pagingParams.page === this.pagingParams.info.totalPages;
                },
                selectPage: function (page, event) {
                    if (event) {event.preventDefault();}
                    if (event.target.getAttribute('disabled')) {
                        return;
                    }
                    this.pagingParams.page = page;
                    this.fetchListings();

                },
                fetchListings: function () {
                    this.isLoading = true;
                    this.$http({
                        url: '/api/dl-listings',
                        headers: {
                            'Authorization': 'Bearer ' + Cookies.get('ql-auth').split(":")[1]
                        },
                        params: {
                            page: this.pagingParams.page - 1,
                            size: this.pagingParams.itemsPerPage,
                            sort: this.pagingParams.sort
                        },
                        method: 'GET'
                    }).then(function (response) {
                        console.log('Success!:', response.data);
                        let totalItems = response.headers.get('X-Total-Count');
                        this.pagingParams.info.queryCount = totalItems;
                        this.pagingParams.info.totalItems = totalItems;
                        let links = parseLinks(response.headers.get('link'));
                        var totalPages = this.calculateTotalPages();
                        this.pagingParams.info.totalPages = totalPages;
                        this.pages = getPages(this.pagingParams.page, totalPages);
                        this.dlListings = response.data;
                        this.isLoading = false;
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.isLoading = false;
                    });
                },
                confirmDeleteListing: function(dlListing) {
                    this.confirmModal.listingToDelete = dlListing;
                    $('#my-modal').modal('show');
                },
                deleteListing: function () {
                    this.$http({
                        url: '/api/dl-listings/' + this.confirmModal.listingToDelete.id,
                        method: 'DELETE'
                    }).then(function (response) {
                        console.log('Success!:', response.data);

                        let index = this.dlListings.indexOf(this.confirmModal.listingToDelete);
                        this.dlListings.splice(index, 1);
                        $.notify({
                            message: jsTranslations['page.my_listings.notifications.delete_listing_success']
                        }, {
                            type: 'success'
                        });
                        this.confirmModal.listingToDelete = null;
                        $('#my-modal').modal('hide');
                    }, function (response) {
                        console.log('Error!:', response.data);
                        $.notify({
                            message: response.data
                        }, {
                            type: 'danger'
                        });
                        this.confirmModal.listingToDelete = null;
                        $('#my-modal').modal('hide');
                    });
                }
            },
            created: function () {
                this.fetchListings();
            }
        });

    }
};