package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.model.User;
import ro.sci.bookwormscommunity.repositories.AdminRepository;
import ro.sci.bookwormscommunity.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    RoleService roleService;

    public List<User> listAllUsers() {
        List<User> list = new ArrayList<>();
        adminRepository.findAll().forEach(list::add);
        return list;
    }

    public void createModerator(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleService.createRoleIfNotFound("ROLE_MODERATOR"));
        user.setRoles(roles);
        userRepository.save(user);

    }
}
