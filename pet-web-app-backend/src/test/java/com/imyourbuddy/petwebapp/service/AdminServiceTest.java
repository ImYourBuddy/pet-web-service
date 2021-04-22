package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addModerByUserIdTestWithoutExceptions() throws ResourceNotFoundException {
        User user = new User(1L, "Test", "test1234", "Test", "Test", new Date(),
                false, false, new ArrayList<Role>());
        Role moderatorRole = new Role(1, "ROLE_MODERATOR");
        List<Role> roles = new ArrayList<>();
        roles.add(moderatorRole);
        User moderator = new User(1L, "Test", "test1234", "Test", "Test", new Date(),
                false, false, roles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_MODERATOR")).thenReturn(moderatorRole);

        User savedUser = adminService.addModerByUserId(1L);

        verify(userRepository).findById(1L);
        verify(roleRepository).findByName("ROLE_MODERATOR");
        verify(userRepository).save(moderator);

        assertEquals(savedUser.getId(), user.getId());
        assertEquals(savedUser.getUsername(), user.getUsername());
        assertEquals(savedUser.getPassword(), user.getPassword());
        assertTrue(savedUser.getRoles().contains(moderatorRole));
    }

    @Test
    public void addModerByUserIdTestWithResourceNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.addModerByUserId(1L));
        verify(userRepository).findById(1L);
        verify(roleRepository, times(0)).findByName("ROLE_MODERATOR");
    }

    @Test
    public void removeModerByUserIdTestWithoutExceptions() throws ResourceNotFoundException {
        User user = new User(1L, "Test", "test1234", "Test", "Test", new Date(),
                false, false, new ArrayList<Role>());
        Role moderatorRole = new Role(1, "ROLE_MODERATOR");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ROLE_MODERATOR")).thenReturn(moderatorRole);

        User savedUser = adminService.removeModerByUserId(1L);

        verify(userRepository).findById(1L);
        verify(roleRepository).findByName("ROLE_MODERATOR");
        verify(userRepository).save(user);

        assertEquals(savedUser.getId(), user.getId());
        assertEquals(savedUser.getUsername(), user.getUsername());
        assertFalse(savedUser.getRoles().contains(moderatorRole));
    }

    @Test
    public void removeModerByUserIdTestWithResourceNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.removeModerByUserId(1L));
        verify(userRepository).findById(1L);
        verify(roleRepository, times(0)).findByName("ROLE_MODERATOR");
    }
}
