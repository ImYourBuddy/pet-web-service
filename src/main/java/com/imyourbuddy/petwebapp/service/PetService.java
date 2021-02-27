package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for {@link Pet}
 */

@Service
public class PetService {
    private final PetRepository repository;

    @Autowired
    public PetService(PetRepository repository) {
        this.repository = repository;
    }

    public List<Pet> getAll() {
        return repository.findAll();
    }
}
