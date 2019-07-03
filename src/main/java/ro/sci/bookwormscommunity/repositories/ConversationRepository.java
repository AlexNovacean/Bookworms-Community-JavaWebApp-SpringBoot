package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Conversation;

import java.util.List;

/**
 * Repository of the {@link Conversation} class.
 * <p>
 * Handles the DB connection and all the operations regarding the DB (create, update, delete, etc.).
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @see JpaRepository
 */
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Transactional
    @Query("SELECT c " +
            "FROM Conversation AS c " +
            "WHERE " +
            "c.fromUser.id IN (:userIdOne, :userIdTwo) " +
            "AND " +
            "c.toUser.id IN (:userIdOne, :userIdTwo) " +
            "AND " +
            "c.conversationTopic = :bookName")
    Conversation getConversation(@Param("userIdOne") long userIdOne, @Param("userIdTwo") long userIdTwo, @Param("bookName") String bookName);

    @Transactional
    @Query("SELECT c " +
            "FROM Conversation AS c " +
            "WHERE " +
            "c.toUser.id = :userId " +
            "OR " +
            "c.fromUser.id = :userId")
    List<Conversation> getConversations(@Param("userId") long userId);

}
