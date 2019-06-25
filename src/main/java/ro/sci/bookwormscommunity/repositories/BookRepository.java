package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Transactional
    @Query("SELECT b " +
            "FROM " +
            "Book AS b " +
            "WHERE " +
            "b.user.id = :userId")
    List<Book> getUserBooks(@Param("userId") long userId);

}
