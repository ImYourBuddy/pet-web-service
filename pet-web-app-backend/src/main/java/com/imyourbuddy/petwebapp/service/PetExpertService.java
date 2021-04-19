package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.projection.PetExpertQueryResult;
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
    private final UserService userService;

    @Autowired
    public PetExpertService(PetExpertRepository petExpertRepository, UserService userService) {
        this.petExpertRepository = petExpertRepository;
        this.userService = userService;
    }

    public List<PetExpertQueryResult> getAll() {
        return petExpertRepository.findAllConfirmedExperts();
    }

    public PetExpert getByUserId(long userId) throws ResourceNotFoundException {
        userService.getById(userId);
        return petExpertRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet expert with user id = " + userId + " not found"));
    }

    public PetExpert save(PetExpert expert) throws ResourceNotFoundException {
        userService.getById(expert.getUserId());
        return petExpertRepository.save(expert);
    }

    public PetExpert edit(long userId, PetExpert updatedExpert) throws ResourceNotFoundException {
        PetExpert expert = getByUserId(updatedExpert.getUserId());
        expert.setQualification(updatedExpert.getQualification());
        expert.setOnlineHelp(updatedExpert.isOnlineHelp());
        petExpertRepository.save(expert);
        return expert;
    }
}
