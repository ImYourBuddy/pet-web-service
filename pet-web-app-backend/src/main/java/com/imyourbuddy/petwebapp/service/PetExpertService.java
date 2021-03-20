package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for {@link PetExpert}
 */

@Service
public class PetExpertService {
    private final PetExpertRepository petExpertRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetExpertService(PetExpertRepository petExpertRepository, UserRepository userRepository) {
        this.petExpertRepository = petExpertRepository;
        this.userRepository = userRepository;
    }

    public List<PetExpert> getAll() {
        return petExpertRepository.findAll();
    }

    public PetExpert save(PetExpert expert) throws ResourceNotFoundException {
        userRepository.findById(expert.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + expert.getUserId() + " not found"));
        return petExpertRepository.save(expert);
    }
}
