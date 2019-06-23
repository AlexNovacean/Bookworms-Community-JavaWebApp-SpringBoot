package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Conversation;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Transactional
    @Query("SELECT c " +
            "FROM Conversation AS c " +
            "WHERE " +
            "c.fromUser.id IN (:userIdOne, :userIdTwo) " +
            "AND " +
            "c.toUser.id IN (:userIdOne, :userIdTwo)")
    Conversation getConversation(@Param("userIdOne") long userIdOne, @Param("userIdTwo") long userIdTwo);

    @Transactional
    @Query("SELECT c " +
            "FROM Conversation AS c " +
            "WHERE " +
            "c.toUser.id = :userId " +
            "OR " +
            "c.fromUser.id = :userId")
    List<Conversation> getConversations(@Param("userId") long userId);

}
