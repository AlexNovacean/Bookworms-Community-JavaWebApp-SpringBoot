package ro.sci.bookwormscommunity.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;
import ro.sci.bookwormscommunity.web.dto.UserDto;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveUserTest() throws Exception {

        List<User> userList = new ArrayList<>();

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                User user1 = (User) arguments[0];
                userList.add(user1);
            }
            return null;
        }).when(userRepository).save(any(User.class));

        UserRegistrationDto userDto = new UserRegistrationDto("John", "Cameron", "Johnny", "Location", "password", "test@mail.com");
        Path path = Paths.get("src/main/resources/static/images/default-picture.png");
        MultipartFile file = new MockMultipartFile("poza", new ByteArrayInputStream(Files.readAllBytes(path)));
        userDto.setImage(file);
        userService.save(userDto);

        assertNotNull(userList);
        assertEquals(1, userList.size());
        assertEquals("John", userList.get(0).getFirstName());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void updateUser() throws Exception {

        User user = new User(1L, "John", "Cameron", "john@email.com", "password", "johnny", "Boston");
        UserDto userDto = new UserDto("John", "Cameron", "Johnny", "password", "test@mail.com", "location");
        Path path = Paths.get("src/main/resources/static/images/default-picture.png");
        MultipartFile file = new MockMultipartFile("poza", new ByteArrayInputStream(Files.readAllBytes(path)));
        userDto.setPhoto(file);
        List<User> userList = new ArrayList<>();
        userList.add(user);

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                User user1 = (User) arguments[0];
                userList.set(0, user1);
            }
            return null;
        }).when(userRepository).save(any(User.class));

        when(userRepository.getOne(anyLong())).thenReturn(user);

        userService.updateUser(1L, userDto);

        assertEquals(1, userList.size());
        assertEquals(user, userList.get(0));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).getOne(anyLong());
    }

    @Test
    public void banUser() throws Exception {
        User user = new User();
        user.setEnabled(true);
        when(userRepository.getOne(anyLong())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.banUser(1L);

        assertFalse(user.isEnabled());
        verify(userRepository, times(1)).getOne(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void promoteUserTest() throws Exception {
        User user = new User(1L, "test@mail.com");
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        Role role = new Role("ROLE_MODERATOR");

        when(roleService.createRoleIfNotFound(anyString())).thenReturn(role);
        when(userRepository.getOne(1L)).thenReturn(user);

        List<User> userList = new ArrayList<>();
        userList.add(user);

        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            User user1 = (User) arguments[0];
            userList.set(0, user1);
            return null;
        }).when(userRepository).save(any(User.class));

        userService.promoteUser(1L);
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.createRoleIfNotFound(anyString()));

        Assert.assertEquals(roles, userList.get(0).getRoles());
        assertNotNull(userList);
        assertEquals(1, userList.size());
        verify(userRepository, times(1)).save(any());
        verify(userRepository, times(1)).getOne(anyLong());
    }

    @Test
    public void loadUserByUsername() {
        User user = new User();
        user.setEmail("joe@email.com");
        user.setPassword("thepassword");
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setEnabled(true);

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        UserDetails foundUser = userService.loadUserByUsername("joe@email.com");

        assertNotNull(foundUser);
        assertEquals("joe@email.com", foundUser.getUsername());
        assertEquals("thepassword", foundUser.getPassword());
    }

    @Test
    public void findByEmailTest() {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        User result = userService.findByEmail("john@email");
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void getAllUsersTest() {

        List<User> list = new ArrayList<>(Arrays.asList(new User(), new User()));
        when(userRepository.findAll()).thenReturn(list);
        List<User> userList = userService.getAllUsers();

        assertEquals(2, userList.size());
        assertEquals(list, userList);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void findByIdTest() throws Exception {
        User user = new User(1L, "test@mail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User resultUser = userService.findById(1L);

        assertNotNull(resultUser);
        assertEquals(user, resultUser);
        verify(userRepository, times(1)).findById(anyLong());
    }
}