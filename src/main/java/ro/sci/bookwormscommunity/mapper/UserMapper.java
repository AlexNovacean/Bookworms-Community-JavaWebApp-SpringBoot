package ro.sci.bookwormscommunity.mapper;

import org.springframework.mock.web.MockMultipartFile;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.web.dto.UserDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Serves as a mapper for the {@link User} class
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 */
public final class UserMapper {

    private UserMapper() {
    }

    /**
     * Maps an {@link User} object to an {@link UserDto} object
     *
     * @param user {@link User} instance
     * @return an {@link UserDto} instance
     * @throws IOException if the {@link MockMultipartFile} object cannot be build
     */
    public static UserDto mapUserToUserDto(User user) throws IOException {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setNickname(user.getNickname());
        userDto.setEmail(user.getEmail());
        userDto.setLocation(user.getLocation());
        userDto.setPhoto(new MockMultipartFile("userPhoto.png", new ByteArrayInputStream(user.getPhoto())));
        return userDto;
    }
}
