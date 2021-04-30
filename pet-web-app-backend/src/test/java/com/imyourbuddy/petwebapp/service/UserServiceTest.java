package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.request.LoginRequest;
import com.imyourbuddy.petwebapp.dto.response.JwtResponse;
import com.imyourbuddy.petwebapp.dto.response.MessageResponse;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.*;
import com.imyourbuddy.petwebapp.repository.PetRepository;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import com.imyourbuddy.petwebapp.security.jwt.JwtUtils;
import com.imyourbuddy.petwebapp.security.jwt.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void authenticateTest() {
        LoginRequest loginRequest = new LoginRequest("username", "password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        Role roleOwner = new Role(1, "ROLE_OWNER");
        List<Role> roles = new ArrayList<>();
        roles.add(roleOwner);
        List<String> stringRoles = new ArrayList<>();
        stringRoles.add("ROLE_OWNER");
        User user = new User(1L, "username", "password", "First name", "Last name",
                null, false, false, roles);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        UsernamePasswordAuthenticationToken fullPopulatedAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        ResponseEntity<JwtResponse> responseEntity =  ResponseEntity.ok().body(new JwtResponse("generated token",
                userDetails.getId(), stringRoles));
        when(authenticationManager.authenticate(authenticationToken)).thenReturn(fullPopulatedAuthentication);
        when(jwtUtils.generateJwtToken(fullPopulatedAuthentication)).thenReturn("generated token");

        ResponseEntity<?> authenticate = userService.authenticate(loginRequest);

        assertEquals(responseEntity, authenticate);
        verify(authenticationManager).authenticate(authenticationToken);
        verify(jwtUtils).generateJwtToken(fullPopulatedAuthentication);

    }

    @Test
    public void registerTestWithBadRequest() {
        User user = new User(1L, "username", "password", "First name", "Last name",
                new Date(), false, false, new ArrayList<>());
        when(userRepository.existsByUsername("username")).thenReturn(true);

        ResponseEntity<?> result = userService.register(user);

        assertEquals(result, ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!")));
        verify(roleRepository, never()).findByName(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void registerTest() {
        User user = new User(1L, "username", "password", "First name", "Last name",
                null, false, false, new ArrayList<>());
        Role roleOwner = new Role(1, "ROLE_OWNER");
        when(userRepository.existsByUsername("username")).thenReturn(false);
        when(roleRepository.findByName("ROLE_OWNER")).thenReturn(roleOwner);
        when(passwordEncoder.encode("password")).thenReturn("encoded password");

        ResponseEntity<?> result = userService.register(user);
        User registeredUser = new User(1L, "username", "encoded password", "First name", "Last name",
                new Date(), false, false, new ArrayList<>());
        UserResponse response = UserResponse.fromUser(registeredUser);

        assertEquals(result, ResponseEntity.ok().body(response));
        verify(userRepository).existsByUsername("username");
        verify(roleRepository).findByName("ROLE_OWNER");
        verify(passwordEncoder).encode("password");
    }

    @Test
    public void getAllTest() {
        Role roleModerator = new Role(1, "ROLE_MODERATOR");
        Role roleOwner = new Role(2, "ROLE_OWNER");
        List<Role> thirdRoles = new ArrayList<>();
        List<Role> firstSecondRoles = new ArrayList<>();
        thirdRoles.add(roleModerator);
        firstSecondRoles.add(roleOwner);
        User firstUser = new User(1L, "username_1", "password_1", "First name_1", "Last name_1",
                new Date(), false, false, firstSecondRoles);
        User secondUser = new User(1L, "username_2", "password_2", "First name_2", "Last name_2",
                new Date(), false, false, firstSecondRoles);
        User thirdUser = new User(1L, "username_3", "password_3", "First name_3", "Last name_3",
                new Date(), false, false, thirdRoles);
        List<User> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);
        users.add(thirdUser);
        UserResponse firstResponse = UserResponse.fromUser(firstUser);
        List<String> firstSecondResponseRoles = new ArrayList<>();
        firstSecondResponseRoles.add("OWNER");
        firstResponse.setRoles(firstSecondResponseRoles);
        UserResponse secondResponse = UserResponse.fromUser(secondUser);
        secondResponse.setRoles(firstSecondResponseRoles);
        List<String> thirdResponseRoles = new ArrayList<>();
        thirdResponseRoles.add("MODERATOR");
        UserResponse thirdResponse = UserResponse.fromUser(thirdUser);
        thirdResponse.setRoles(thirdResponseRoles);
        List<UserResponse> response = new ArrayList<>();
        response.add(thirdResponse);
        response.add(firstResponse);
        response.add(secondResponse);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAll();
        assertEquals(result, response);
    }

    @Test
    public void getByIdTestWithResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    public void getUserByTokenTestWithResourceNotFoundException() {
        when(jwtUtils.getUserNameFromJwtToken("token")).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByToken("token"));
        verify(jwtUtils).getUserNameFromJwtToken("token");
        verify(userRepository).findByUsername("username");
    }

    @Test
    public void editProfileTest() throws ResourceNotFoundException {
        User user = new User(1L, "username", "password", "First name", "Last name",
                new Date(), false, false, new ArrayList<>());
        User editedUser = new User(1L, "username", "password", "edited first name",
                "edited last name", user.getCreated(), false, false, new ArrayList<>());
        EditUserRequest request = new EditUserRequest("edited first name", "edited last name");
        UserResponse response = UserResponse.fromUser(editedUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.editUser(1L, request);
        verify(userRepository).save(editedUser);
        assertEquals(result, response);

    }

    @Test
    public void getPetByIdTestWithResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getPetById(1L, 1L));

        Pet pet = new Pet(1L, PetSpecies.CAT, "Kitty", "Ordinal cat", PetGender.FEMALE,
                new Date(), 1L);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.findByOwner(1L)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNotFoundException.class, () -> userService.getPetById(1L, 1L));
    }

    @Test
    public void getPetByIdTest() throws ResourceNotFoundException {
        Pet firstPet = new Pet(1L, PetSpecies.CAT, "Kitty", "Ordinal cat", PetGender.FEMALE,
                new Date(), 1L);
        Pet secondPet = new Pet(2L, PetSpecies.DOG, "Doggy", "Ordinal dog", PetGender.MALE,
                new Date(), 1L);
        List<Pet> pets = new ArrayList<>();
        pets.add(firstPet);
        pets.add(secondPet);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(petRepository.findByOwner(1L)).thenReturn(pets);
        when(petRepository.findById(2L)).thenReturn(Optional.of(secondPet));

        Pet foundPet = userService.getPetById(1L, 2L);

        verify(petRepository).findByOwner(1L);
        verify(petRepository).findById(2L);
        assertEquals(foundPet, secondPet);

    }

    @Test
    public void editPetTest() throws ResourceNotFoundException {
        Pet pet = new Pet(1L, PetSpecies.CAT, "Kitty", "Ordinal cat", PetGender.FEMALE,
                new Date(), 1L);
        Pet editedPet = new Pet(1L, PetSpecies.DOG, "Doggy", "Ordinal dog", PetGender.MALE,
                new Date(), 1L);
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(petRepository.findByOwner(1L)).thenReturn(pets);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        userService.editPet(1L, 1L, editedPet);
        verify(petRepository).findByOwner(1L);
        verify(petRepository).findById(1L);
        verify(petRepository).save(editedPet);
    }


}
