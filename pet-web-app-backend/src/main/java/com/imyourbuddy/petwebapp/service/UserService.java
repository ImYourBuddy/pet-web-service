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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    public ResponseEntity<JwtResponse> authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                roles));
    }

    public ResponseEntity<?> register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        Role reader = roleRepository.findByName("ROLE_READER");
        List<Role> userRoles = new ArrayList<>();
        Timestamp createdDate = new Timestamp(new Date().getTime());
        userRoles.add(reader);
        user.setRoles(userRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(createdDate);
        UserResponse userResponse = UserResponse.fromUser(user);
        userRepository.save(user);
        return ResponseEntity.ok().body(userResponse);
    }

    public List<UserResponse> getAll() {
        List<User> users = userRepository.findAll();
        List<UserResponse> responses = new LinkedList<>();
        users.forEach(user -> {
            List<String> roles = new LinkedList<>();
            user.getRoles().forEach(role -> {
                roles.add(role.getName().substring(5));
            });
            UserResponse userResponse = UserResponse.fromUser(user);
            userResponse.setRoles(roles);
            responses.add(userResponse);
        });
        return responses;
    }

    public User getByLogin(String login) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(login);
        if (foundUser == null) {
            throw new ResourceNotFoundException("User with login = " + login + "not found.");
        }
        return foundUser.orElse(new User());
    }

    public User getById(long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + "not found."));
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
        foundUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        UserResponse userResponse = UserResponse.fromUser(foundUser);
        userRepository.save(foundUser);

        return userResponse;
    }

    public Pet addPet(Pet pet) throws ResourceNotFoundException {
        User foundUser = getById(pet.getOwner());
        Role role_owner = roleRepository.findByName("ROLE_OWNER");
        List<Role> roles = foundUser.getRoles();
        roles.add(role_owner);
        foundUser.setRoles(roles);
        userRepository.save(foundUser);
        petRepository.save(pet);

        return pet;
    }

    public List<Pet> getAllPetsById(long ownerId) throws ResourceNotFoundException {
        getById(ownerId);
        List<Pet> pets = petRepository.findByOwner(ownerId);
        if (pets.size() == 0) {
            throw new ResourceNotFoundException("User with id = " + ownerId + " don't have pets.");
        }
        return pets;
    }

    public Pet deletePet(long ownerId, long petId) throws ResourceNotFoundException {
        List<Pet> allPetsById = getAllPetsById(ownerId);
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new ResourceNotFoundException("Pet with id = " + petId + " not found.");
        }
        allPetsById.remove(pet);
        petRepository.delete(pet);
        if (allPetsById.size() == 0) {
            User owner = getById(ownerId);
            List<Role> roles = owner.getRoles();
            Role role_owner = roleRepository.findByName("ROLE_OWNER");
            roles.remove(role_owner);
            owner.setRoles(roles);
            userRepository.save(owner);
        }
        return pet;
    }

    public List<User> getAllUserSummaries(long id) {
        return userRepository.findAll()
                .stream()
                .filter(user -> !(user.getId() == id))
                .collect(Collectors.toList());
    }
}
