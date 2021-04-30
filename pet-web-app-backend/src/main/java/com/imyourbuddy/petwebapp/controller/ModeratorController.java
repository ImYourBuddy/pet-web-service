package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestQueryResult;
import com.imyourbuddy.petwebapp.service.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User banUserById(@PathVariable(name = "userId") long userId,
                                            @RequestBody BanRequest banRequest) throws ResourceNotFoundException, IllegalOperationException {
        return moderatorService.banUserById(userId, banRequest.getDescription());
    }

    @DeleteMapping ("/users/{userId}/ban")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public User unbanUserById(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException, IllegalOperationException {
        return moderatorService.unbanUserById(userId);
    }

    @GetMapping("/experts")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public List<PetExpertRequestQueryResult> getExpertRequests() {
        return moderatorService.getExpertRequests();
    }

    @PatchMapping("/experts/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public PetExpert confirmExpert(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException, IllegalOperationException {

         return moderatorService.confirmExpert(userId);
    }

    @DeleteMapping("/experts/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public PetExpert rejectExpert(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException, IllegalOperationException {
        return moderatorService.rejectExpert(userId);
    }

    @DeleteMapping("/experts/{userId}/delete")
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('MODERATOR')")
    public PetExpert deleteExpert(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return moderatorService.deleteExpert(userId);
    }
}
