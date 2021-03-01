package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.service.PetExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
