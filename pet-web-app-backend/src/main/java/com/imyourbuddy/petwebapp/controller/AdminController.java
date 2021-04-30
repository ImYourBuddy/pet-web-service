package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.service.AdminService;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PatchMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public User deleteUserById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return userService.deleteUserById(id);
    }

    @PostMapping("/users/{id}/moder")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User addModerByUserId(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return adminService.addModerByUserId(id);
    }

    @DeleteMapping("/users/{id}/moder")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public User removeModerByUserId(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return adminService.removeModerByUserId(id);
    }


}
