package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Query("SELECT m FROM " +
            "Message AS m " +
            "WHERE " +
            "m.conversation.id = :convId")
    List<Message> getUserMessages(@Param("convId") long convId);

}
