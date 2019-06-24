package ro.sci.bookwormscommunity.mapper;

import org.springframework.mock.web.MockMultipartFile;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.web.dto.BookDto;

public class BookMapper {

    public static BookDto mapBookToBookDto(Book book){
        BookDto bookDto = new BookDto();
        bookDto.setUser(book.getUser());
        bookDto.setBookName(book.getBookName());
        bookDto.setAuthorName(book.getAuthorName());
        bookDto.setBookRent(book.isBookRent());
        bookDto.setBookSale(book.isBookSale());
        bookDto.setCondition(book.getCondition());
        bookDto.setDescription(book.getDescription());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setNumberOfPages(book.getNumberOfPages());
        bookDto.setType(book.getType());
        bookDto.setRentPrice(book.getRentPrice());
        bookDto.setSellPrice(book.getSellPrice());
        bookDto.setPhoto(new MockMultipartFile("bookPhoto.png",book.getImage()));
        bookDto.setId(book.getId());

        return bookDto;
    }
}
