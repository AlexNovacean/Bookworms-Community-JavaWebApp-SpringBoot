//package ro.sci.bookwormscommunity;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ro.sci.bookwormscommunity.model.User;
//import ro.sci.bookwormscommunity.repositories.RoleRepository;
//import ro.sci.bookwormscommunity.repositories.UserRepository;
//import ro.sci.bookwormscommunity.service.UserService;
//import ro.sci.bookwormscommunity.service.UserServiceImpl;
//import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;
//
//import javax.validation.constraints.NotNull;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.Java6Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.AdditionalAnswers.returnsFirstArg;
//import static org.mockito.Mockito.when;
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BookwormsCommunityApplicationTests {
//
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//    }
//    @Mock
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @InjectMocks
//    private UserServiceImpl userServiceImp;
//
//    @Test
//    public void contextLoads() {
//    }
//
//    @Test
//    public void saveUserTest() {
//        UserRegistrationDto userDto = new UserRegistrationDto("John", "Cameron", "Johnny", "Boston", "john@email.com", "john@email.com");
//        User user = new User();
//        user.setFirstName(userDto.getFirstName());
//        user.setLastName(userDto.getLastName());
//        user.setNickname(userDto.getNickName());
//        user.setLocation(userDto.getLocation());
//        user.setEmail(userDto.getEmail());
////        when(userRepository.("john@email.com").thenReturn(user);
//
//        when(userRepository.save(user)).thenReturn(user);
////        Mockito.doReturn(user).when(userRepository).save(user);
//        User user1 = userServiceImp.save(user);
//        Assert.assertEquals(user, user1);
//    }
//
//    @Test
//    public void findByEmailTest() {
//        UserRegistrationDto userDto = new UserRegistrationDto("John", "Cameron", "Johnny", "Boston", "john@email.com", "john@email.com");
//        User user = new User();
//        user.setFirstName(userDto.getFirstName());
//        user.setLastName(userDto.getLastName());
//        user.setNickname(userDto.getNickName());
//        user.setLocation(userDto.getLocation());
//        user.setEmail(userDto.getEmail());
////        when(userRepository.("john@email.com").thenReturn(user);
//        when(userRepository.findByEmail("john@email")).thenReturn(user);
////        Mockito.doReturn(user).when(userRepository).findByEmail("john@email");
//        User user1 = userServiceImp.findByEmail("john@email");
//        Assert.assertEquals(user, user1);
//    }
//
//    @Test
//    public void getAllUsersTest(){
//        User user1 = new User(1l,"John", "Cameron", "john@email.com", "johnny", "Boston");
//        User user2 = new User(1l,"Mary", "Carrie", "mary@email.com", "mar", "New York");
//        User user3 = new User(1l,"Joe", "Obrien", "joe@email.com", "jo", "Rochester");
//
//        List<User> list = new ArrayList<>();
//        list.add(user1);
//        list.add(user2);
//        list.add(user3);
//        when(userRepository.findAll()).thenReturn(list);
//
//        List<User> userList = userServiceImp.getAllUsers();
//        Assert.assertEquals(3, userList.size());
//    }
//
//    @Test
//    public void findByIdTest(){
//        User user = new User(1l,"John", "Cameron", "john@email.com", "johnny", "Boston");
////        Mockito.doReturn(user).when(userRepository).findById(1l);
//        when(userRepository.findById(1l)).thenReturn(java.util.Optional.of(user));
//        User user1 = userServiceImp.findById(1l);
//        Assert.assertEquals("John", user1.getFirstName());
//        Assert.assertEquals("Cameron", user1.getLastName());
//        Assert.assertEquals("john@email.com", user1.getEmail());
//        Assert.assertEquals("johnny", user1.getNickname());
//        Assert.assertEquals("Boston", user1.getLocation());
//    }
////
////    @Test
////    public void givenHomePageURI_whenMockMVC_thenReturnsIndexJSPViewName() {
////        this.mockMvc.perform(get("/homePage")).andDo(print())
////
////                .andExpect(view().name("index"));
////    }
//
//
////    @Test
////    public void saveUserTest() {
//////        User user = new User(1l,"John", "Cameron", "john@email.com", "johnny", "Boston");
////        when(userRepository.save(any(User.class))).thenReturn(user);
//////        User user1;
//////        user1 = userService.findById(user.getId());
//////        Assert.assertEquals(user, userService.findById(1l));
////    }
//
//
//
//
//}
