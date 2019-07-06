package ro.sci.bookwormscommunity.model;

import com.opencsv.bean.CsvBindByName;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

/**
 * POJO class representing the users of the application.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @CsvBindByName(column = "User Id")
    private Long id;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "E-mail")
    private String email;

    private String password;

    @CsvBindByName(column = "Nickname")
    private String nickname;

    @CsvBindByName(column = "Location")
    private String location;

    @Column(columnDefinition = "bool default true")
    @CsvBindByName(column = "Account Enabled")
    private boolean enabled;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Book> books;

    public User() {
    }

    public User(long id, String email) throws IOException {
        this();
        Path path = Paths.get("src/main/resources/static/images/default-picture.png");
        this.id = id;
        this.email = email;
        this.photo = Files.readAllBytes(path);
    }

    public User(String firstName, String lastName, String email, String nickname, String location) {
        this.nickname = nickname;
        this.location = location;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Encodes the {@link User} image field's byte array with the {@link Base64} encoder and returns it as a String.
     *
     * @return a the {@link User}'s image field as a String encoded with the {@link Base64} encode.
     */
    public String getImageAsString() {
        return Base64.getEncoder().encodeToString(this.photo);
    }
}