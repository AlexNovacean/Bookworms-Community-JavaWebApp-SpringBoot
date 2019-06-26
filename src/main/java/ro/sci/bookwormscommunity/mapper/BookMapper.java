package ro.sci.bookwormscommunity.mapper;

import org.springframework.mock.web.MockMultipartFile;
import ro.sci.bookwormscommunity.model.Book;
import ro.sci.bookwormscommunity.web.dto.BookDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public final class BookMapper {

    private BookMapper() {
    }

    public static BookDto mapBookToBookDto(Book book) throws IOException {
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
        bookDto.setPhoto(new MockMultipartFile("bookPhoto.png", new ByteArrayInputStream(book.getImage())));
        bookDto.setId(book.getId());
        return bookDto;
    }

    public static Book mapBookDtoToBook(BookDto bookDto) throws Exception {
        Book book = new Book();
        book.setBookName(bookDto.getBookName());
        book.setAuthorName(bookDto.getAuthorName());
        book.setNumberOfPages(bookDto.getNumberOfPages());
        book.setType(bookDto.getType());
        book.setLanguage(bookDto.getLanguage());
        book.setDescription(bookDto.getDescription());
        book.setCondition(bookDto.getCondition());
        book.setBookRent(bookDto.isBookRent());
        book.setBookSale(bookDto.isBookSale());
        book.setSellPrice(bookDto.getSellPrice());
        book.setRentPrice(bookDto.getRentPrice());
        book.setImage(bookDto.returnPhoto().getBytes());
        book.setUser(bookDto.getUser());
        return book;
    }
}
