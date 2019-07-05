package ro.sci.bookwormscommunity.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import ro.sci.bookwormscommunity.model.Role;
import ro.sci.bookwormscommunity.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService = new RoleServiceImpl();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRoleIfNotFound_existingRole() {
        Role role = new Role("ROLE_TEST");
        when(roleRepository.findByName(anyString())).thenReturn(role);

        Role result = roleService.createRoleIfNotFound("ROLE_TEST");

        assertNotNull(result);
        assertEquals(role, result);
        verify(roleRepository, times(1)).findByName(anyString());
    }

    @Test
    public void createRoleIfNotFound_non_existingROle() {
        List<Role> roles = new ArrayList<>();

        when(roleRepository.findByName(anyString())).thenReturn(null);
        doAnswer((InvocationOnMock invocation) -> {
            Object[] arguments = invocation.getArguments();
            Role role = (Role) arguments[0];
            roles.add(role);
            return null;
        }).when(roleRepository).save(any(Role.class));

        Role result = roleService.createRoleIfNotFound("ROLE_TEST");

        assertNotNull(result);
        assertNotNull(roles);
        assertTrue(roles.contains(result));
        assertTrue(roles.stream().anyMatch(r -> r.getName().equals(result.getName())));
        verify(roleRepository, times(1)).findByName(anyString());
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}