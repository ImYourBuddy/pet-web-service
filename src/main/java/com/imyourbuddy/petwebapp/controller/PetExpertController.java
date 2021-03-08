package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.dto.response.UserResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.service.PetExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for {@link PetExpert}
 */

@RestController
@RequestMapping("/rest/pet-expert")
public class PetExpertController {
    private final PetExpertService service;

    @Autowired
    public PetExpertController(PetExpertService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<PetExpert> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public PetExpert add(@RequestBody PetExpert expert) {
        return service.save(expert);
    }

}
