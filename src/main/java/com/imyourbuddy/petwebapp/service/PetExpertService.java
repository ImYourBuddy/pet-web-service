package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for {@link PetExpert}
 */

@Service
public class PetExpertService {
    private final PetExpertRepository repository;

    @Autowired
    public PetExpertService(PetExpertRepository repository) {
        this.repository = repository;
    }

    public List<PetExpert> getAll() {
        return repository.findAll();
    }

    public PetExpert save(PetExpert expert) {
        return repository.save(expert);
    }
}
