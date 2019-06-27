package ro.sci.bookwormscommunity.web.dto;

import org.hibernate.validator.constraints.Range;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.validators.ValidPhoto;

import javax.validation.constraints.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class BookDto {

    private Long id;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String bookName;

    @NotEmpty
    @Size(min = 2, max = 100)
    private String authorName;

    //TODO: fix the Failed to convert property value of type java.lang.String to required type int for property numberOfPages;
    // nested exception is java.lang.NumberFormatException: For input string: "" in html

    @NotNull(message = "Please provided a valid number of pages.")
    @Range(min = 20, max = 9999, message = "The number of pages must be a positive number between 20 and 9999")
    private Integer numberOfPages;

    @NotEmpty
    private String type;

    @NotEmpty
    private String language;

    @NotEmpty
    @Size(max = 3000, message = "Description must not exceed 3000 characters!")
    private String description;

    @ValidPhoto(message = "Profile picture must be a .jpg/.jpeg/.png file and must not exceed 1MB.")
    private MultipartFile photo;

    private User user;
    private String condition;
    private boolean bookRent;
    private boolean bookSale;
    private Integer sellPrice;
    private Integer rentPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isBookRent() {
        return bookRent;
    }

    public void setBookRent(boolean bookRent) {
        this.bookRent = bookRent;
    }

    public boolean isBookSale() {
        return bookSale;
    }

    public void setBookSale(boolean bookSale) {
        this.bookSale = bookSale;
    }

    public Integer getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Integer sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Integer getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Integer rentPrice) {
        this.rentPrice = rentPrice;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public MultipartFile returnPhoto() throws Exception {
        if (photo.isEmpty()) {
            return new MockMultipartFile("book.png", new FileInputStream(new File("src/main/resources/static/images/book.png")));
        }
        return photo;
    }

    public byte[] returnUpdatePhoto(byte[] image) throws Exception {
        if (!photo.isEmpty()) {
            return photo.getBytes();
        }
        return image;
    }

    public String getPhotoAsString() throws IOException {
        return Base64.getEncoder().encodeToString(this.photo.getBytes());
    }
}
