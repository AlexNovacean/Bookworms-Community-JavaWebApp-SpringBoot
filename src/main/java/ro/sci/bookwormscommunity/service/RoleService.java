package ro.sci.bookwormscommunity.service;

import ro.sci.bookwormscommunity.model.Role;

public interface RoleService {
    Role createRoleIfNotFound(String roleName);
}
