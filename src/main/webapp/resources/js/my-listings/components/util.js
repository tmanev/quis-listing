
/**
 * @return {boolean}
 */
const twoWordValidator = function (value) {
    if (value) {
        let s = value;
        s = s.replace(/(^\s*)|(\s*$)/gi, "");
        s = s.replace(/[ ]{2,}/gi, " ");
        s = s.replace(/\n /, "\n");
        return s.split(' ').length >= 2;
    }
    return false;
};

/**
 * @return {boolean}
 */
const listHasSelectionValidator = function (selectedValue) {
    return selectedValue != -1;
};

const listingDtoToListingForm = function (dlListingDTO) {
    return {
        id: dlListingDTO.id,
        title: dlListingDTO.title,
        content: dlListingDTO.content,
        name: dlListingDTO.name,
        status: dlListingDTO.status,
        dlCategories: dlListingDTO.dlCategories,
        dlLocations: dlListingDTO.dlLocations,
        dlListingFields: dlListingDTO.dlListingFields,
        attachments: dlListingDTO.attachments,
        featuredAttachment: dlListingDTO.featuredAttachment
    }
};



