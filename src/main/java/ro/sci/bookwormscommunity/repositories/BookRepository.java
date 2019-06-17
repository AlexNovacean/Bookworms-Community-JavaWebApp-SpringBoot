package ro.sci.bookwormscommunity.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.web.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {


}
