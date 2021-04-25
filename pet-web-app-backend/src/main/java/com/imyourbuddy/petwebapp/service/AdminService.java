package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Admin service class for {@link User} class.
 */

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User addModerByUserId(long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
        Role roleModerator = roleRepository.findByName("ROLE_MODERATOR");
        List<Role> roles = user.getRoles();
        roles.add(roleModerator);
        return userRepository.save(user);
    }

    public User removeModerByUserId(long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
        Role roleModerator = roleRepository.findByName("ROLE_MODERATOR");
        List<Role> roles = user.getRoles();
        roles.remove(roleModerator);
        return userRepository.save(user);
    }
}
