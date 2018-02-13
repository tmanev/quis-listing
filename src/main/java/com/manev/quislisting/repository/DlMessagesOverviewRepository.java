package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.DlMessageOverview;
import com.manev.quislisting.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface that holds common methods for retrieving messages by receiver.
 */
@Transactional
public interface DlMessagesOverviewRepository extends JpaRepository<DlMessageOverview, Long> {

    /**
     * Returns all messages by receiver.
     *
     * @param pageable {@link Pageable}
     * @param receiver {@link Long} user that received the message
     * @return {@link Page} of {@link DlMessage}
     */
    @Query("select dlmo from DlMessageOverview dlmo where (receiver = :receiver or sender = :receiver) and deleted = null")
    Page<DlMessageOverview> findAllMessagesByReceiver(Pageable pageable, @Param("receiver") Long receiver);

    /**
     * Return {@link DlMessageOverview} by listingId.
     *
     * @param listingId {@link Long} listing id of the message
     * @return {@link DlMessage} the message
     */
    @Query("select dlmo from DlMessageOverview dlmo where listingId = :listingId and deleted = null")
    DlMessageOverview findOneByListingId(@Param("listingId") Long listingId);

    /**
     * Return {@link DlMessageOverview} either by sender or receiver.
     *
     * @param sender {@link User} user that sent the message
     * @param receiver {@link Long} user that received the message
     * @return {@link DlMessage} the message
     */
    @Query("select dlmo from DlMessageOverview dlmo where deleted = null and (sender = :sender and receiver = :receiver) or (sender = :receiver or receiver = :sender)")
    DlMessageOverview findOneBySenderOrReceiver(@Param("sender") User sender, @Param("receiver") Long receiver);

}
