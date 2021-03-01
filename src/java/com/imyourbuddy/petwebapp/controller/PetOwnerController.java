package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.PetOwner;
import com.imyourbuddy.petwebapp.service.PetOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for {@link PetOwner}
 */

@RestController
@RequestMapping("rest/pet-owner")
public class PetOwnerController {
    private final PetOwnerService service;

    @Autowired
    public PetOwnerController(PetOwnerService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<PetOwner> getAll() {
        return service.getAll();
    }
}
