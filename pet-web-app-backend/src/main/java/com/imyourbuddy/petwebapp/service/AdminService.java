package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
import com.imyourbuddy.petwebapp.repository.PostRepository;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PetExpertRepository expertRepository;
    private final PostRepository postRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminService(UserRepository userRepository, PetExpertRepository expertRepository, PostRepository postRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.expertRepository = expertRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
    }

    public User addModerById(long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + " not found"));
        Role roleModerator = roleRepository.findByName("ROLE_MODERATOR");
        List<Role> roles = user.getRoles();
        roles.add(roleModerator);
        userRepository.save(user);
        return user;
    }

    public User removeModerById(long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + " not found"));
        Role roleModerator = roleRepository.findByName("ROLE_MODERATOR");
        List<Role> roles = user.getRoles();
        roles.remove(roleModerator);
        userRepository.save(user);
        return user;
    }
}
