package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlMessage;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface that holds common methods for retrieving messages between users.
 */
@Transactional
public interface DlMessagesRepository extends JpaRepository<DlMessage, Long> {

    /**
     * Returns all messages between two users.
     *
     * @param pageable {@link Pageable}
     * @param listingId {@link Long} id of the listing
     * @param receiver {@link Long} user that received the message
     * @param sender {@link Long} user that send the message
     * @return {@link Page} of {@link DlMessage}
     */
    @Query("select dlm from DlMessage dlm where listingId = :listingId and (receiver = :receiver or receiver = :sender) and (sender = :sender or sender = :receiver) and deleted = null order by created_date")
    Page<DlMessage> findAllMessagesForListingId(Pageable pageable, @Param("listingId") Long listingId,
            @Param("receiver") Long receiver, @Param("sender") Long sender);

    /**
     * Returns {@List} of {@link DlMessage} based on the listing id.
     *
     * @param listingId {@link Long} id of the listing
     * @return {@List} of {@link DlMessage}
     */
    @Query("select dlm from DlMessage dlm where listingId = :listingId")
    List<DlMessage> findAllByListingId(@Param("listingId") Long listingId);
}
