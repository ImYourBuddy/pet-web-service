package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<UserResponse> editProfile(@PathVariable(name = "id") long id,
                                                    @RequestBody EditUserRequest request) throws ResourceNotFoundException {
        UserResponse userResponse = userService.editProfile(id, request);

        return ResponseEntity.ok().body(userResponse);
    }

    @PostMapping("/add-pet/{id}")
    public ResponseEntity<Pet> addPet(@PathVariable(name = "id") long id, @RequestBody Pet pet) throws ResourceNotFoundException {
        Pet addedPet = userService.addPet(id, pet);
        return ResponseEntity.ok().body(addedPet);
    }

    @GetMapping("/pets/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Pet> getAllPetsById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return userService.getAllPetsById(id);
    }

    @DeleteMapping("/delete-pet/{id}")
    @PreAuthorize("hasRole('OWNER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Pet> deletePet(@PathVariable(name = "id") long id, @RequestBody Pet pet) throws ResourceNotFoundException {
        Pet deletedPet = userService.deletePet(id, pet.getId());
        return ResponseEntity.ok().body(deletedPet);
    }


}
