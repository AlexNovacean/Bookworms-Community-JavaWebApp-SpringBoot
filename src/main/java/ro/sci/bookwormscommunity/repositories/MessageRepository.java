package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Query(value = "SELECT * FROM " +
            "messages AS m, conversation AS c, messages_conversation AS mc " +
            "WHERE " +
            "m.id=mc.message_id AND " +
            "c.id=mc.conversation_id AND " +
            "c.id= :convId", nativeQuery = true)
    List<Message> getUserMessages(@Param("convId") long convId);

}
