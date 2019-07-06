package ro.sci.bookwormscommunity.model;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * A POJO class representing the book objects of the application.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @CsvBindByName(column = "Book Id")
    private Long id;
    @CsvBindByName(column = "Book Name")
    private String bookName;
    @CsvBindByName(column = "Book Author")
    private String authorName;
    @CsvBindByName(column = "Number of Pages")
    private int numberOfPages;
    @CsvBindByName(column = "Book Type")
    private String type;
    @CsvBindByName(column = "Book Language")
    private String language;
    @Column(length = 3000)
    private String description;
    @CsvBindByName(column = "Book Condition")
    private String condition;
    @CsvBindByName(column = "Book Rent")
    private boolean bookRent;
    @CsvBindByName(column = "Book Sale")
    private boolean bookSale;
    @CsvBindByName(column = "Sell Price")
    @Column(columnDefinition = "int default 0")
    private int sellPrice;
    @CsvBindByName(column = "Rent Price")
    @Column(columnDefinition = "int default 0")
    private int rentPrice;
    @Lob
    private byte[] image;
    @ManyToOne
    private User user;
    @Column(columnDefinition = "int default 0")
    private int rating;
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    private Date addDate;

    public Book() {
        this.addDate = new Date();
    }

    public Book(long id) throws IOException {
        this();
        Path path = Paths.get("src/main/resources/static/images/book.png");
        this.id = id;
        this.image = Files.readAllBytes(path);
    }

    public Book(String bookName, String authorName, String type, boolean bookRent, boolean bookSale, int rating) {
        this();
        this.bookName = bookName;
        this.authorName = authorName;
        this.type = type;
        this.bookRent = bookRent;
        this.bookSale = bookSale;
        this.rating = rating;
    }

    public Book(long id, String bookName, String authorName, int numberOfPages, String type, String language, String description, String condition, boolean bookRent, boolean bookSale, int sellPrice, int rentPrice, User user) throws IOException {
        Path path = Paths.get("src/main/resources/static/images/book.png");
        this.bookName = bookName;
        this.authorName = authorName;
        this.numberOfPages = numberOfPages;
        this.type = type;
        this.language = language;
        this.description = description;
        this.condition = condition;
        this.bookRent = bookRent;
        this.bookSale = bookSale;
        this.sellPrice = sellPrice;
        this.rentPrice = rentPrice;
        this.image = Files.readAllBytes(path);
        this.user = user;
        this.id = id;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
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

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    /**
     * Encodes the {@link Book} image field's byte array with the {@link Base64} encoder and returns it as a String.
     *
     * @return a the {@link Book}'s image field as a String encoded with the {@link Base64} encode.
     */
    public String getImageAsString() {
        return Base64.getEncoder().encodeToString(this.image);
    }
}
