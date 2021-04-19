package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestQueryResult;
import com.imyourbuddy.petwebapp.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/moder")
@CrossOrigin("*")
public class ModeratorController {
    private final ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @PostMapping("/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> banUserById(@PathVariable(name = "userId") long userId,
                                            @RequestBody BanRequest banRequest) throws ResourceNotFoundException {
        User user = moderatorService.banUserById(userId, banRequest.getDescription());
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping ("/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<User> unbanUserById(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        User user = moderatorService.unbanUserById(userId);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/experts")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public List<PetExpertRequestQueryResult> getExpertRequest() {
        return moderatorService.getExpertRequest();
    }

    @PatchMapping("/experts/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<PetExpert> confirmExpert(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {

         PetExpert petExpert = moderatorService.confirmExpert(userId);
         return ResponseEntity.ok().body(petExpert);
    }

    @DeleteMapping("/experts/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<PetExpert> rejectExpert(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        PetExpert petExpert = moderatorService.rejectExpert(userId);
        return ResponseEntity.ok().body(petExpert);
    }

    @DeleteMapping("/experts/{userId}/delete")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public ResponseEntity<PetExpert> deleteModer(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        PetExpert petExpert = moderatorService.deleteExpert(userId);
        return ResponseEntity.ok().body(petExpert);
    }
}
