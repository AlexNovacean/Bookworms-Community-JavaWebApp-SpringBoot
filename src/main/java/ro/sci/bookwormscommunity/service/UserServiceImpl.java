package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.UserRepository;
import ro.sci.bookwormscommunity.web.dto.UserDto;
import ro.sci.bookwormscommunity.web.dto.UserRegistrationDto;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.isEnabled(),true,true,true,getAuthorities(user));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void save(UserRegistrationDto userDto) throws Exception {

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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(long id, UserDto userDto) throws Exception {
        User user = userRepository.getOne(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickname(userDto.getNickname());
        user.setLocation(userDto.getLocation());
        user.setEmail(userDto.getEmail());
        if(!userDto.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setPhoto(userDto.returnUpdatePhoto(user.getPhoto()));
        userRepository.save(user);
    }

    public void banUser(long id){
        User user = userRepository.getOne(id);
        user.setEnabled(false);
        userRepository.save(user);
    }
}
