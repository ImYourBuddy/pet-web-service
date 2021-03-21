package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.dto.request.ExpertRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.ExpertRequestProjection;
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

    @PostMapping("/ban")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> banUserById(@RequestBody BanRequest request) throws ResourceNotFoundException {
        User user = userService.banUserById(request.getUserId(), request.isBanned());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/add-moder")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<User> addModerById(@RequestBody User user) throws ResourceNotFoundException {
        User moder = userService.addModerById(user.getId());
        return ResponseEntity.ok().body(moder);
    }

    @GetMapping("/experts")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public List<ExpertRequestProjection> getExpertRequest() {
        return userService.getExpertRequest();
    }

    @PatchMapping("/confirm-expert")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public void confirmExpert(@RequestBody ExpertRequest expert) throws ResourceNotFoundException {
        adminService.confirmExpert(expert.getUserId(), expert.getExpertId());
    }
}
