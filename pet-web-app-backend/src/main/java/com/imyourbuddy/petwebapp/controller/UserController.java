package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.security.jwt.UserDetailsImpl;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> editProfile(@PathVariable(name = "id") long id,
                                                    @RequestBody @Valid EditUserRequest request) throws ResourceNotFoundException {
        UserResponse userResponse = userService.editProfile(id, request);

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        User user = userService.getById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/{userId}/pets")
    public ResponseEntity<Pet> addPet(@PathVariable(name = "userId") long userId,
            @RequestBody @Valid Pet pet) throws ResourceNotFoundException {
        Pet addedPet = userService.addPet(userId, pet);
        return ResponseEntity.ok().body(addedPet);
    }

    @GetMapping("/{userId}/pets")
    @PreAuthorize("hasRole('OWNER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Pet> getAllPetsById(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return userService.getAllPetsByUserId(userId);
    }

    @DeleteMapping("/{userId}/pets/{petId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Pet> deletePet(@PathVariable(name = "userId") long ownerId,
                                         @PathVariable(name = "petId") long petId) throws ResourceNotFoundException {
        Pet deletedPet = userService.deletePet(ownerId, petId);
        return ResponseEntity.ok().body(deletedPet);
    }

    @GetMapping("/{userId}/pets/{petId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Pet> getPet(@PathVariable(name = "userId") long ownerId,
                                         @PathVariable(name = "petId") long petId) throws ResourceNotFoundException {
        Pet pet = userService.getPetById(ownerId, petId);
        return ResponseEntity.ok().body(pet);
    }

    @PutMapping("/{ownerId}/pets/{petId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Pet> editPet(@PathVariable(name = "ownerId") long ownerId,
                                      @PathVariable(name = "petId") long petId, @RequestBody Pet updatedPet) throws ResourceNotFoundException {
        Pet pet = userService.editPet(ownerId, petId, updatedPet);
        return ResponseEntity.ok().body(pet);
    }



}
