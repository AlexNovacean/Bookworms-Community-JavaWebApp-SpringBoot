package ro.sci.bookwormscommunity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sci.bookwormscommunity.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {


}
