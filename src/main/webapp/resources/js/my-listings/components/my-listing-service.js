MyListingService = {
    createListing: function (listing) {
        return axios.post('/api/dl-listings', listing, {
            headers: {
                'Authorization': QlUtil.Rest.authorizationBearer()
            }
        });
    },
    updateListingPartial: function (listing) {
        return axios.patch('/api/dl-listings', listing, {
            headers: {
                'Authorization': QlUtil.Rest.authorizationBearer()
            }
        });
    },
    updateListing: function (listing) {
        return axios.put('/api/dl-listings', listing, {
            headers: {
                'Authorization': QlUtil.Rest.authorizationBearer()
            }
        });
    },
    publishListing: function (listing) {
        return axios.put('/api/dl-listings/publish', listing, {
            headers: {
                'Authorization': QlUtil.Rest.authorizationBearer()
            }
        });
    },
    deleteListing: function (id) {
        return axios.delete('/api/dl-listings/' + id, {
            headers: {
                'Authorization': QlUtil.Rest.authorizationBearer()
            }
        });
    },
    componentsValid: function (allComponents) {
        let allValid = true;

        for (let i = 0; i < allComponents.length; i++) {
            if (!allComponents[i].isInputInvalid()) {
                allValid = false;
            }
        }

        return allValid;
    }
};