package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Ban;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.repository.BanRepository;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
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

class ModeratorServiceTest {

    @InjectMocks
    private ModeratorService moderatorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PetExpertRepository expertRepository;

    @Mock
    private BanRepository banRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void banUserByIdTestWithResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.banUserById(1L, "Test ban"));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).banUserById(1L, true);
        verify(banRepository, never()).save(any());
    }

    @Test
    public void banUserByIdTestWithIllegalOperationException() {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                true, false, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalOperationException.class, () -> moderatorService.banUserById(1L, "Test ban"));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).banUserById(1L, true);
        verify(banRepository, never()).save(any());
    }


    @Test
    public void banUserByIdTestWithoutExceptions() throws ResourceNotFoundException, IllegalOperationException {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, new ArrayList<>());
        Ban ban = new Ban(1L, "Test ban");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        moderatorService.banUserById(1L, "Test ban");
        verify(userRepository).findById(1L);
        verify(userRepository).banUserById(1L, true);
        verify(banRepository).save(ban);
    }

    @Test
    public void unbanUserByIdTestWithUserResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.unbanUserById(1L));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).banUserById(1L, false);
        verify(banRepository, never()).delete(any());
    }

    @Test
    public void unbanUserByIdTestWithBanResourceNotFoundException() {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                true, false, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(banRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.unbanUserById(1L));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).banUserById(1L, false);
        verify(banRepository, never()).delete(any());
    }

    @Test
    public void unbanUserByIdTestWithIllegalOperationException() {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(IllegalOperationException.class, () -> moderatorService.unbanUserById(1L));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).banUserById(1L, false);
        verify(banRepository, never()).delete(any());
    }

    @Test
    public void unbanUserByIdTestWithoutExceptions() throws ResourceNotFoundException, IllegalOperationException {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                true, false, new ArrayList<>());
        Ban ban = new Ban(1L, "Test ban");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(banRepository.findByUserId(1L)).thenReturn(Optional.of(ban));

        moderatorService.unbanUserById(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).banUserById(1L, false);
        verify(banRepository).delete(ban);
    }

    @Test
    public void confirmExpertTestWithUserResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.confirmExpert(1L));
        verify(userRepository).findById(1L);
        verify(expertRepository, never()).findByUserId(anyLong());
        verify(roleRepository, never()).findByName(anyString());
        verify(userRepository, never()).save(any());
        verify(expertRepository, never()).confirmExpert(anyLong());
    }

    @Test
    public void confirmExpertTestWithExpertResourceNotFoundException() {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.confirmExpert(1L));
        verify(userRepository).findById(1L);
        verify(expertRepository).findByUserId(1L);
        verify(roleRepository, never()).findByName(anyString());
        verify(userRepository, never()).save(any());
        verify(expertRepository, never()).confirmExpert(anyLong());
    }

    @Test
    public void confirmExpertTestWithIllegalOperationException() {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, new ArrayList<>());
        PetExpert expert = new PetExpert(1L, "Test qualification", true, 1L, 0L,
                true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));

        assertThrows(IllegalOperationException.class, () -> moderatorService.confirmExpert(1L));
        verify(userRepository).findById(1L);
        verify(expertRepository).findByUserId(1L);
        verify(roleRepository, never()).findByName(anyString());
        verify(userRepository, never()).save(any());
        verify(expertRepository, never()).confirmExpert(anyLong());
    }

    @Test
    public void confirmExpertTestWithout() throws ResourceNotFoundException, IllegalOperationException {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, new ArrayList<>());
        PetExpert expert = new PetExpert(1L, "Test qualification", true, 1L, 0L,
                false);
        Role expertRole = new Role(1, "ROLE_EXPERT");
        List<Role> roles = new ArrayList<>();
        roles.add(expertRole);
        User expertUser = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, roles);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));
        when(roleRepository.findByName("ROLE_EXPERT")).thenReturn(expertRole);

        moderatorService.confirmExpert(1L);
        System.out.println(user);

        verify(userRepository).findById(1L);
        verify(expertRepository).findByUserId(1L);
        verify(roleRepository).findByName("ROLE_EXPERT");
        verify(userRepository).save(expertUser);
        verify(expertRepository).confirmExpert(1L);
    }

    @Test
    public void rejectExpertTestWithResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.rejectExpert(1L));
        verify(expertRepository).findByUserId(1L);
        verify(expertRepository, never()).delete(any());
    }

    @Test
    public void rejectExpertTestWithIllegalOperationException() {
        PetExpert expert = new PetExpert(1L, "Test qualification", true, 1L, 0L,
                true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));

        assertThrows(IllegalOperationException.class, () -> moderatorService.rejectExpert(1L));
        verify(expertRepository).findByUserId(1L);
        verify(expertRepository, never()).delete(expert);
    }

    @Test
    public void rejectExpertTestWithoutException() throws ResourceNotFoundException, IllegalOperationException {
        PetExpert expert = new PetExpert(1L, "Test qualification", true, 1L, 0L,
                false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));

        moderatorService.rejectExpert(1L);

        verify(expertRepository).findByUserId(1L);
        verify(expertRepository).delete(expert);
    }

    @Test
    public void deleteExpertTestWithResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> moderatorService.deleteExpert(1L));
        verify(userRepository).findById(1L);
        verify(expertRepository).findByUserId(1L);
        verify(roleRepository, never()).findByName(anyString());
        verify(expertRepository, never()).delete(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void deleteExpertWithoutExceptions() throws ResourceNotFoundException {
        User user = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, new ArrayList<>());
        PetExpert expert = new PetExpert(1L, "Test qualification", true, 1L, 0L,
                true);
        Role expertRole = new Role(1, "ROLE_EXPERT");
        List<Role> roles = new ArrayList<>();
        roles.add(expertRole);
        User expertUser = new User(1L, "test", "test1234", "Test", "User", new Date(),
                false, false, roles);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expertUser));
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));
        when(roleRepository.findByName("ROLE_EXPERT")).thenReturn(expertRole);

        moderatorService.deleteExpert(1L);

        verify(userRepository).findById(1L);
        verify(expertRepository).findByUserId(1L);
        verify(roleRepository).findByName("ROLE_EXPERT");
        verify(userRepository).save(user);
        verify(expertRepository).delete(expert);
    }




}
