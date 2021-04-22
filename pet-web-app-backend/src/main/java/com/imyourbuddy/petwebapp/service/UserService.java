package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.dto.request.LoginRequest;
import com.imyourbuddy.petwebapp.dto.response.JwtResponse;
import com.imyourbuddy.petwebapp.dto.response.MessageResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.repository.PetRepository;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import com.imyourbuddy.petwebapp.security.jwt.JwtUtils;
import com.imyourbuddy.petwebapp.security.jwt.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for {@link User} class.
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.petRepository = petRepository;
    }

    public ResponseEntity<?> authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (userDetails.isBanned()) {
            return ResponseEntity.badRequest().body(new MessageResponse("You are banned!"));
        }
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                roles));
    }

    public ResponseEntity<?> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Username is already taken!"));
        }
        Role owner = roleRepository.findByName("ROLE_OWNER");
        List<Role> userRoles = new ArrayList<>();
        Timestamp createdDate = new Timestamp(new Date().getTime());
        userRoles.add(owner);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(createdDate);
        UserResponse userResponse = UserResponse.fromUser(user);
        userRepository.save(user);
        return ResponseEntity.ok().body(userResponse);
    }

    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = new ArrayList<>();
        users.forEach(user -> {
            List<String> roles = new ArrayList<>();
            user.getRoles().forEach(role -> {
                roles.add(role.getName().substring(5));
            });
            UserResponse userResponse = UserResponse.fromUser(user);
            userResponse.setRoles(roles);
            responses.add(userResponse);
        });
        responses.sort((u1, u2) -> {
            if (u1.getRoles().contains("MODERATOR") && !u2.getRoles().contains("MODERATOR")) {
                return -1;
            } else if ((u1.getRoles().contains("MODERATOR") && u2.getRoles().contains("MODERATOR"))
                    || (!u1.getRoles().contains("MODERATOR") && !u2.getRoles().contains("MODERATOR"))) {
                return 0;
            } else {
                return 1;
            }
        });
        return responses;
    }

    public User getById(long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + "not found."));
    }

    public User getUserByToken(String token) throws ResourceNotFoundException {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username = " + username + " not found"));
    }

    public User deleteUserById(long id) throws ResourceNotFoundException {
        User user = getById(id);
        userRepository.deleteUserById(id);
        return user;
    }

    public UserResponse editProfile(long id, EditUserRequest userRequest) throws ResourceNotFoundException {
        User foundUser = getById(id);

        foundUser.setFirstName(userRequest.getFirstName());
        foundUser.setLastName(userRequest.getLastName());
        UserResponse userResponse = UserResponse.fromUser(foundUser);
        userRepository.save(foundUser);

        return userResponse;
    }

    public Pet addPet(long userId, Pet pet) throws ResourceNotFoundException {
        User foundUser = getById(userId);
        pet.setOwner(userId);
        petRepository.save(pet);

        return pet;
    }

    public List<Pet> getAllPetsByUserId(long ownerId) throws ResourceNotFoundException {
        getById(ownerId);
        List<Pet> pets = petRepository.findByOwner(ownerId);
        return pets;
    }

    public Pet getPetById(long ownerId, long petId) throws ResourceNotFoundException {
        List<Pet> allPetsById = getAllPetsByUserId(ownerId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet with id = " + petId + " not found."));
        if (!allPetsById.contains(pet)) {
            throw new ResourceNotFoundException("User with id = " + ownerId + " don't have pet with id = " + petId);
        }
        return pet;
    }

    public Pet deletePet(long ownerId, long petId) throws ResourceNotFoundException {
        Pet pet = getPetById(ownerId, petId);
        petRepository.delete(pet);
        return pet;
    }

    public Pet editPet(long ownerId, long petId, Pet updatedPet) throws ResourceNotFoundException {
        Pet pet = getPetById(ownerId, petId);
        pet.setName(updatedPet.getName());
        pet.setSpecies(updatedPet.getSpecies());
        pet.setBreed(updatedPet.getBreed());
        pet.setGender(updatedPet.getGender());
        pet.setBirthdate(updatedPet.getBirthdate());
        petRepository.save(pet);
        return pet;
    }
}