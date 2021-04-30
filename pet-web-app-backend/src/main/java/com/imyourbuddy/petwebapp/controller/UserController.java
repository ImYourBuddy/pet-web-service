package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public UserResponse editUser(@PathVariable(name = "id") long id,
                                    @RequestBody @Valid EditUserRequest request) throws ResourceNotFoundException {
        return userService.editUser(id, request);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return userService.getById(id);
    }

    @PostMapping("/{userId}/pets")
    public Pet addPet(@PathVariable(name = "userId") long userId,
                                      @RequestBody @Valid Pet pet) throws ResourceNotFoundException {
        return userService.addPet(userId, pet);
    }

    @GetMapping("/{userId}/pets")
    public List<Pet> getAllPetsByUserId(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return userService.getAllPetsByUserId(userId);
    }

    @DeleteMapping("/{userId}/pets/{petId}")
    public Pet deletePet(@PathVariable(name = "userId") long ownerId,
                                         @PathVariable(name = "petId") long petId) throws ResourceNotFoundException {
        return userService.deletePet(ownerId, petId);
    }

    @GetMapping("/{userId}/pets/{petId}")
    public Pet getPetById(@PathVariable(name = "userId") long ownerId,
                                      @PathVariable(name = "petId") long petId) throws ResourceNotFoundException {
        return userService.getPetById(ownerId, petId);
    }

    @PutMapping("/{ownerId}/pets/{petId}")
    public Pet editPet(@PathVariable(name = "ownerId") long ownerId,
                                       @PathVariable(name = "petId") long petId, @RequestBody @Valid Pet updatedPet) throws ResourceNotFoundException {
        return userService.editPet(ownerId, petId, updatedPet);
    }


}
