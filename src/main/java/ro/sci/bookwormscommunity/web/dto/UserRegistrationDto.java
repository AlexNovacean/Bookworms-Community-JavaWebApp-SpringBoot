package ro.sci.bookwormscommunity.web.dto;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ro.sci.bookwormscommunity.validators.FieldMatch;
import ro.sci.bookwormscommunity.validators.ValidPhoto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.FileInputStream;


@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})
public class UserRegistrationDto {

    @NotEmpty(message = "Please provide your First Name")
    private String firstName;

    @NotEmpty(message = "Please provide your Last Name")
    private String lastName;

    @NotEmpty(message = "Please provide a Nickname")
    @Size(min = 4, max = 32, message = "Nickname size must be between 4 and 32")
    private String nickName;

    @NotEmpty(message = "Please provided the name of the city you live in")
    private String location;

    @NotEmpty(message = "Please provide a password")
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters")
    private String password;

    @NotEmpty(message = "Please confirm your password")
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters")
    private String confirmPassword;

    @Email(message = "Please provide a valid email address")
    @NotEmpty(message = "Please provide an email address")
    private String email;

    @Email(message = "Please provide a matching email address")
    @NotEmpty(message = "Please provide an email address")
    private String confirmEmail;

    @ValidPhoto(message = "Profile picture must be a .jpg/.jpeg/.png file and must not exceed 1MB.")
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile returnImage() throws Exception {
        if (image.isEmpty()) {
            return new MockMultipartFile("default-picture.png", new FileInputStream(new File("src/main/resources/static/images/default-picture.png")));
        }
        return image;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }
}