package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.Moderator;
import com.imyourbuddy.petwebapp.repository.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for {@link Moderator}
 */

@Service
public class ModeratorService {
    private final ModeratorRepository repository;

    @Autowired
    public ModeratorService(ModeratorRepository repository) {
        this.repository = repository;
    }

    public List<Moderator> getAll() {
        return repository.findAll();
    }
}
