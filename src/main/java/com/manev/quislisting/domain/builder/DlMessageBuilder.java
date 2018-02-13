package com.manev.quislisting.domain.builder;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import java.time.ZonedDateTime;

/**
 * @author <a href="mailto:dimitar.gavrilov@publicispixelpark.de">Dimitar Gavrilov</a>
 */
public class DlMessageBuilder {

    private Long id;
    private Long receiver;
    private String text;
    private Long listingId;
    private ZonedDateTime createdDate;

    private DlMessageBuilder() {
    }

    public static DlMessageBuilder dlMessage() {
        return new DlMessageBuilder();
    }

    public DlMessageBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public DlMessageBuilder withReceiver(final Long receiver) {
        this.receiver = receiver;
        return this;
    }

    public DlMessageBuilder withText(final String text) {
        this.text = text;
        return this;
    }

    public DlMessageBuilder withListingId(final Long listingId) {
        this.listingId = listingId;
        return this;
    }

    public DlMessageBuilder withCreatedDate(final ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public DlMessageOverview buildDlMessageOverview() {
        final DlMessageOverview dlMessageOverview = new DlMessageOverview();
        dlMessageOverview.setId(id);
        dlMessageOverview.setReceiver(receiver);
        dlMessageOverview.setText(text);
        dlMessageOverview.setListingId(listingId);
        dlMessageOverview.setCreatedDate(createdDate);

        return dlMessageOverview;
    }

    public DlMessage buildDlMessage() {
        final DlMessage dlMessage = new DlMessage();
        dlMessage.setId(id);
        dlMessage.setReceiver(receiver);
        dlMessage.setText(text);
        dlMessage.setListingId(listingId);
        dlMessage.setCreatedDate(createdDate);

        return dlMessage;
    }

}
