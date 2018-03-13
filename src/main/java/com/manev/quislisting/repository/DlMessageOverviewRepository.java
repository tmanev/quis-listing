package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface that holds common methods for retrieving messages by receiver.
 */
@Transactional
public interface DlMessageOverviewRepository extends JpaRepository<DlMessageOverview, Long>, DlMessageOverviewRepositoryCustom {

    /**
     * Return {@link DlMessageOverview} either by sender or receiver.
     *
     * @param listingId {@link Long} Listing id
     * @param sender {@link User} user that sent the message
     * @param receiver {@link User} user that received the message
     * @return {@link DlMessage} the message
     */
    @Query("select dlmo from DlMessageOverview dlmo where dlmo.listingId=:listingId and dlmo.sender = :sender and dlmo.receiver = :receiver")
    DlMessageOverview findOneByDlListingAndSenderAndReceiver(@Param("listingId") Long listingId, @Param("sender") User sender, @Param("receiver") User receiver);

}
