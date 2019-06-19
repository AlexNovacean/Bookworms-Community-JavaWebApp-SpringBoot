package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m" +
            " FROM " +
            "Message m " +
            "WHERE " +
            "m.fromUser.id IN (:userIdOne, :userIdTwo) " +
            "AND " +
            "m.toUser.id in (:userIdOne, :userIdTwo) " +
            "ORDER BY " +
            "m.sentDate " +
            "DESC")
    List<Message> getExistingMessages(
            @Param("userIdOne") long userIdOne, @Param("userIdTwo") long userIdTwo);

    @Transactional
    @Query("SELECT m " +
            "FROM " +
            "Message AS m " +
            "WHERE " +
            "m.toUser.id = :userId")
    List<Message> getUserMessages(@Param("userId")long userId);

}
