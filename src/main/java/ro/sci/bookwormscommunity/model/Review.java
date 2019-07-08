package ro.sci.bookwormscommunity.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

/**
 * POJO class representing the review given by users to a any particular book.
 * <p>
 * It has two important parts: the rating which is a number between 1 and 5, and the comment.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userNickname;

    @Column(length = 1000)
    @NotEmpty(message = "Please provide a comment for your review.")
    private String comment;

    @Min(value = 1, message = "Please provide a rating with your review.")
    private int rating;

    private Date created;

    @ManyToOne
    private Book book;

    @Column(columnDefinition = "bool default false")
    private boolean edited;

    private String editedBy;

    @Lob
    private byte[] userPhoto;

    private long userId;

    public Review() {
        this.created = new Date();
    }

    public Review(long id) throws IOException {
        this();
        Path path = Paths.get("src/main/resources/static/images/book.png");
        this.id = id;
        this.userPhoto = Files.readAllBytes(path);
    }

    public Review(int rating) {
        this();
        this.rating = rating;
    }

    public Review(long id, String comment, boolean edited, String editedBy) throws IOException {
        this(id);
        this.comment = comment;
        this.edited = edited;
        this.editedBy = editedBy;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte[] getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(byte[] userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserPhotoAsString() {
        return Base64.getEncoder().encodeToString(this.userPhoto);
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
