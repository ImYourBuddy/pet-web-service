package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/pet")
public class PetController {
    public final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/add")
    public ResponseEntity<Pet> add(@RequestBody Pet pet) {
        Pet savedPet = petService.save(pet);
        return ResponseEntity.ok().body(savedPet);
    }

}
