package ro.sci.bookwormscommunity.web.dto;

import org.springframework.web.multipart.MultipartFile;
import ro.sci.bookwormscommunity.validators.FieldMatch;
import ro.sci.bookwormscommunity.validators.ValidPhoto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.Base64;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
})
public class UserDto {

    private long id;

    @NotEmpty(message = "Please provide your First Name")
    private String firstName;

    @NotEmpty(message = "Please provide your Last Name")
    private String lastName;

    @NotEmpty(message = "Please provide a Nickname")
    @Size(min = 4, max = 32, message = "Nickname size must be between 4 and 32")
    private String nickname;

    private String password;

    private String confirmPassword;

    @Email(message = "Please provide a valid email address")
    @NotEmpty(message = "Please provide an email address")
    private String email;

    @NotEmpty(message = "Please provided the name of the city you live in")
    private String location;

    @ValidPhoto(message = "Profile picture must be a .jpg/.jpeg/.png file and must not exceed 1MB.")
    private MultipartFile photo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public byte[] returnUpdatePhoto(byte[] image) throws IOException {
        if (!photo.isEmpty()) {
            return photo.getBytes();
        }
        return image;
    }

    public String returnPhotoAsString() throws IOException {
        return Base64.getEncoder().encodeToString(photo.getBytes());
    }
}
