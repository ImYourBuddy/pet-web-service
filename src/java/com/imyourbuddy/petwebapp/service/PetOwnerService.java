package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.PetOwner;
import com.imyourbuddy.petwebapp.repository.PetOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for {@link PetOwner}
 */

@Service
public class PetOwnerService {
    private final PetOwnerRepository repository;


    @Autowired
    public PetOwnerService(PetOwnerRepository repository) {
        this.repository = repository;
    }

    public List<PetOwner> getAll() {
        return repository.findAll();
    }
}
