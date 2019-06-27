package ro.sci.bookwormscommunity.model;

import javax.persistence.*;
import java.util.Base64;
import java.util.List;

/**
 * <h1>Book</h1>
 * A generic class
 * It is used to create the elements within the classes.
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String bookName;

    private String authorName;

    private int numberOfPages;

    private String type;

    private String language;

    @Column(length = 3000)
    private String description;

    private String condition;

    private boolean bookRent;

    private boolean bookSale;

    private int sellPrice;

    private int rentPrice;

    @Lob
    private byte[] image;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "int default 0")
    private int rating;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<Review> reviews;

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

    public String getImageAsString() {
        return Base64.getEncoder().encodeToString(this.image);
    }
}
