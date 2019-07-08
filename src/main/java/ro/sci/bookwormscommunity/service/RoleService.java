package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Role;

/**
 * Services that creates a role if it doesn't already exist.
 *
 * @author Alex
 * @author Ionut
 * @author Radu
 * @author Sorin
 */
public interface RoleService {
    Role createRoleIfNotFound(String roleName);
}
