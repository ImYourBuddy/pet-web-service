package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetExpertServiceTest {

    @InjectMocks
    private PetExpertService expertService;

    @Mock
    private UserService userService;

    @Mock
    private PetExpertRepository expertRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getByUserIdTestWithResourceNotFoundException() throws ResourceNotFoundException {
        when(expertRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> expertService.getByUserId(1L));
        verify(userService).getById(1L);
        verify(expertRepository).findByUserId(1L);
    }

    @Test
    public void editTest() throws ResourceNotFoundException {
        PetExpert expert = new PetExpert(1L, "Test qualification", false, 1L, 10L,
                true);
        PetExpert updatedUser = new PetExpert(10L, "Updated test qualification", true, 10L,
                1L, true);

        when(expertRepository.findByUserId(1L)).thenReturn(Optional.of(expert));

        PetExpert editedUser = expertService.edit(1L, updatedUser);

        assertEquals(1L, editedUser.getId());
        assertEquals("Updated test qualification", editedUser.getQualification());
        assertTrue(editedUser.isOnlineHelp());
        assertEquals(1L, editedUser.getUserId());
        assertEquals(10L, editedUser.getReputation());

    }

}
