package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.dto.request.ExpertRequest;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestProjection;
import com.imyourbuddy.petwebapp.service.ModeratorService;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/moder")
@CrossOrigin("*")
public class ModeratorController {
    private final UserService userService;
    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(UserService userService, ModeratorService moderatorService) {
        this.userService = userService;
        this.moderatorService = moderatorService;
    }

    @PostMapping("/ban")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> banUserById(@RequestBody BanRequest request) throws ResourceNotFoundException {
        User user = moderatorService.banUserById(request);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping ("/ban/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> unbanUserById(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        User user = moderatorService.unbanUserById(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/experts")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public List<PetExpertRequestProjection> getExpertRequest() {
        return moderatorService.getExpertRequest();
    }

    @PatchMapping("/experts")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public void confirmExpert(@RequestBody ExpertRequest expert) throws ResourceNotFoundException {
        moderatorService.confirmExpert(expert.getUserId(), expert.getExpertId());
    }

    @DeleteMapping("/experts/{expertId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public void rejectExpert(@PathVariable(name = "expertId") long expertId) throws ResourceNotFoundException {
        moderatorService.rejectExpert(expertId);
    }

    @DeleteMapping("/experts/delete/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public void deleteModer(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        moderatorService.deleteExpert(userId);
    }
}
