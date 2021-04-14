package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.service.AdminService;
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
    private final AdminService adminService;

    @Autowired
    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public List<UserResponse> getAllUsers() {
        return userService.getAll();
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> deleteUserById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        User user = userService.deleteUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users/{id}/add-moder")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<User> addModerById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        User moder = adminService.addModerById(id);
        return ResponseEntity.ok().body(moder);
    }

    @DeleteMapping("/users/{id}/remove-moder")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<User> removeModerById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        User moder = adminService.removeModerById(id);
        return ResponseEntity.ok().body(moder);
    }


}
