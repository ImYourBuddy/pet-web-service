package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public List<UserResponse> getAllUsers() {
        return userService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> deleteUserById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        User user = userService.deleteUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/ban")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> banUserById(@RequestBody BanRequest request) throws ResourceNotFoundException {
        User user = userService.banUserById(request.getUserId(), request.isBanned());
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping("/add-moder")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<User> addModerById(@RequestBody Long id) throws ResourceNotFoundException {
        User moder = userService.addModerById(id);
        return ResponseEntity.ok().body(moder);
    }
}