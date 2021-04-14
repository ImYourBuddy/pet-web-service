package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.projection.PetExpertProjection;
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

    public List<PetExpertProjection> getAll() {
        List<PetExpertProjection> allExperts = petExpertRepository.findAllConfirmedExperts();
        return allExperts;
    }

    public PetExpert getByUserId(long userId) throws ResourceNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
        PetExpert petExpert = petExpertRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet expert with user id = " + userId + " not found"));
        return petExpert;
    }

    public PetExpert save(PetExpert expert) throws ResourceNotFoundException {
        userRepository.findById(expert.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + expert.getUserId() + " not found"));
        return petExpertRepository.save(expert);
    }

    public PetExpert edit(PetExpert updatedExpert) throws ResourceNotFoundException {
        PetExpert expert = getByUserId(updatedExpert.getUserId());
        expert.setQualification(updatedExpert.getQualification());
        expert.setOnlineHelp(updatedExpert.isOnlineHelp());
        petExpertRepository.save(expert);
        return expert;
    }
}
