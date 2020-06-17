package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Message;

import java.util.List;

/**
 * Repository of the {@link Message} class.
 * <p>
 * Handles the DB connection and all the operations regarding the DB (create, update, delete, etc.).
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see JpaRepository
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Query("SELECT m FROM " +
            "Message AS m " +
            "WHERE " +
            "m.conversation.id = :conversationId")
    List<Message> getUserMessages(@Param("conversationId") long conversationId);

}
