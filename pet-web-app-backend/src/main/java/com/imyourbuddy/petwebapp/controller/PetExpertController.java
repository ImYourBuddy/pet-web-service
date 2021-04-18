package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.projection.PetExpertQueryResult;
import com.imyourbuddy.petwebapp.service.PetExpertService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PetExpertController(PetExpertService service) {
        this.service = service;
    }

    @GetMapping()
    @PreAuthorize("hasRole('OWNER')")
    public List<PetExpertQueryResult> getAll() {
        return service.getAll();
    }


    @GetMapping("/{userId}/check")
    @PreAuthorize("hasRole('OWNER')")
    public boolean checkExpertByUserId(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return service.checkExpertByUserId(userId);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('OWNER')")
    public PetExpert getByUserId(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return service.getByUserId(userId);
    }

    @PutMapping()
    @PreAuthorize("hasRole('EXPERT')")
    public PetExpert edit(@RequestBody @Valid PetExpert updatedExpert) throws ResourceNotFoundException {
        return service.edit(updatedExpert);
    }


    @PostMapping()
    @PreAuthorize("hasRole('OWNER')")
    public PetExpert add(@RequestBody @Valid PetExpert expert) throws ResourceNotFoundException {
        return service.save(expert);
    }


}
