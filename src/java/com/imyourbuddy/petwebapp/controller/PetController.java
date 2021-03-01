package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for {@link Pet}
 */

@RestController
@RequestMapping("/rest/pet")
public class PetController {
    private final PetService service;

    @Autowired
    public PetController(PetService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Pet> getAll() {
        return service.getAll();
    }
}
