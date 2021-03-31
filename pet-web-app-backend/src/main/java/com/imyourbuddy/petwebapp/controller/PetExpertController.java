package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.projection.PetExpertProjection;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestProjection;
import com.imyourbuddy.petwebapp.service.MessageService;
import com.imyourbuddy.petwebapp.service.PetExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for {@link PetExpert}
 */

@RestController
@RequestMapping("/rest/pet-expert")
@CrossOrigin(origins = "*")
public class PetExpertController {
    private final PetExpertService service;
    private final MessageService messageService;

    @Autowired
    public PetExpertController(PetExpertService service, MessageService messageService) {
        this.service = service;
        this.messageService = messageService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('READER') or hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<PetExpertProjection> getAll() {
        return service.getAll();
    }

    @PostMapping()
    public PetExpert add(@RequestBody @Valid PetExpert expert) throws ResourceNotFoundException {
        return service.save(expert);
    }


}
