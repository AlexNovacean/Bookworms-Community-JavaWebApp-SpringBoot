package ro.sci.bookwormscommunity.web.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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

    @Size(min=2, max=15)
    private String bookName;
    @Size(min=2, max=15)
    private String authorName;
    @Max(1000)
    @Min(5)
    private int numberOfPages;
    private String type;
    private String language;
    private String description;
    private String condition;
    private boolean bookRent;
    private boolean bookSale;
    private double sellPrice;
    private double rentPrice;


    public Book() {
    }

    public Book(String bookName, String authorName, int numberOfPages, String type, String language, byte[] imageData, String imageFileName, String description, String condition, boolean bookRent, boolean bookSale, double rating, double sellPrice, double rentPrice) {

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

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", type='" + type + '\'' +
                ", language='" + language + '\'' +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                ", bookRent=" + bookRent +
                ", bookSale=" + bookSale +
                ", sellPrice=" + sellPrice +
                ", rentPrice=" + rentPrice +
                '}';
    }
}
