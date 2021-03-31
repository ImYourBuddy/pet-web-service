package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestProjection;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeratorService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PetExpertRepository expertRepository;

    @Autowired
    public ModeratorService(UserRepository userRepository, RoleRepository roleRepository, PetExpertRepository expertRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.expertRepository = expertRepository;
    }

    public User banUserById(long id, boolean banned) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + " not found"));
        userRepository.banUserById(id, banned);
        return user;
    }

    public List<PetExpertRequestProjection> getExpertRequest() {
        return userRepository.getExpertRequest();
    }

    public void confirmExpert(long userId, long expertId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
        expertRepository.findById(expertId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert with id = " + expertId + " not found"));
        Role role_expert = roleRepository.findByName("ROLE_EXPERT");
        List<Role> roles = user.getRoles();
        roles.add(role_expert);
        userRepository.save(user);
        expertRepository.confirmExpert(expertId);
    }
}
