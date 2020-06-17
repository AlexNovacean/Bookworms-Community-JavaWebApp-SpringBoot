package ro.sci.bookwormscommunity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.repositories.RoleRepository;

/**
 * Implementation for the {@link RoleService}.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 * @author Raul
 * @see RoleService
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Checks if a {@link Role} object with the provided name exists and returns it if found.
     * If a role with the provided name is not found, a new {@link Role} object with the provided name will be created, saved to DB and returned.
     *
     * @param roleName the name of the role.
     * @return a {@link Role} instance.
     */
    @Override
    public Role createRoleIfNotFound(String roleName) {
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }
        return role;
    }
}
