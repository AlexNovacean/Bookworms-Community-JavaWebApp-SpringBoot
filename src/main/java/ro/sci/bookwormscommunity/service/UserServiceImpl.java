package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;
import ro.sci.bookwormscommunity.web.dto.UserDto;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Implementation for the {@link UserService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see UserService
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(userRoles);
    }

    /**
     * Finds a user entity based on the username(email in this implementation) and is used by an instance of {@link org.springframework.security.authentication.AuthenticationProvider} in order to authenticate a user.
     *
     * @param email the username based on which the user entity is found.
     * @return {@link org.springframework.security.core.userdetails.User} model containing information used in the authentication process
     * @throws UsernameNotFoundException if no user corresponding with the provided username(email) is found
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user));
    }

    /**
     * Returns an {@link User} object, identified with the provided email.
     *
     * @param email identifier for the user
     * @return {@link User} instance.
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Returns an {@link User} object, identified with the provided id.
     *
     * @param id identifier for the user
     * @return {@link User} instance.
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    /**
     * Creates a {@link User} object, populates its fields with the information from the {@link UserDto}, and saves it to the DB.
     *
     * @param userDto {@link UserDto} object containing the values for the {@link User} instance that will be saved
     * @throws IOException if the image file from the userDto cannot be retrieved.
     */
    @Override
    public void save(UserRegistrationDto userDto) throws IOException {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickname(userDto.getNickName());
        user.setLocation(userDto.getLocation());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Arrays.asList(roleService.createRoleIfNotFound("ROLE_USER")));
        user.setPhoto(userDto.returnImage().getBytes());
        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Returns all the existing {@link User} object from the DB.
     *
     * @return a list with all the existing {@link User} instances.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates an existing {@link User} object with the information from the provided {@link UserDto}.
     *
     * @param userId  identifier for the user which will be updated.
     * @param userDto {@link UserDto} object which contains the new values for the user that will be updated.
     * @throws IOException if the image file cannot be retrieved from the {@link UserDto} object
     */
    @Override
    public void updateUser(long userId, UserDto userDto) throws IOException {
        User user = userRepository.getOne(userId);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickname(userDto.getNickname());
        user.setLocation(userDto.getLocation());
        user.setEmail(userDto.getEmail());
        if (!userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setPhoto(userDto.returnUpdatePhoto(user.getPhoto()));
        userRepository.save(user);
    }

    /**
     * Disables a specific user account.
     *
     * @param id identifier for the user who's account will be disabled
     */
    @Override
    public void banUser(long id) {
        User user = userRepository.getOne(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    /**
     * Changes the user's Role to moderator.
     *
     * @param userId identifier for the user that will be promoted
     */
    @Override
    public void promoteUser(long userId) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.createRoleIfNotFound("ROLE_MODERATOR"));
        User user = userRepository.getOne(userId);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
