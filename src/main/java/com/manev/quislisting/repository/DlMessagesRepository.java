package com.manev.quislisting.repository;

import com.manev.quislisting.domain.DlMessage;
import com.manev.quislisting.domain.User;
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
     * @param pageable       {@link Pageable}
     * @param listingId      {@link Long} id of the listing
     * @param sender {@link User} user that send the message
     * @param receiver {@link User} user that received the message
     * @return {@link Page} of {@link DlMessage}
     */
    @Query("select dlm from DlMessage dlm where dlm.listingId = :listingId and (dlm.sender = :sender or dlm.sender = :receiver) and (dlm.receiver = :receiver or dlm.receiver = :sender) order by dlm.created desc")
    Page<DlMessage> findAllMessages(Pageable pageable, @Param("listingId") Long listingId, @Param("sender") User sender, @Param("receiver") User receiver);

}
