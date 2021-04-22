package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Ban;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestQueryResult;
import com.imyourbuddy.petwebapp.repository.BanRepository;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
import com.imyourbuddy.petwebapp.repository.RoleRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Moderator service class for {@link User} class.
 */

@Service
public class ModeratorService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PetExpertRepository expertRepository;
    private final BanRepository banRepository;

    @Autowired
    public ModeratorService(UserRepository userRepository, RoleRepository roleRepository, PetExpertRepository expertRepository, BanRepository banRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.expertRepository = expertRepository;
        this.banRepository = banRepository;
    }

    public User banUserById(long userId, String description) throws ResourceNotFoundException {
        User user = checkUser(userId);
        userRepository.banUserById(userId, true);
        Ban ban = new Ban(userId, description);
        banRepository.save(ban);
        return user;
    }

    public User unbanUserById(long userId) throws ResourceNotFoundException {
        User user = checkUser(userId);
        if (user.isBanned()) {
            userRepository.banUserById(userId, false);
        }
        Ban ban = banRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Ban for user with id = " + userId + " not found"));
        banRepository.delete(ban);
        return user;
    }

    public List<PetExpertRequestQueryResult> getExpertRequest() {
        return expertRepository.getExpertRequest();
    }

    public PetExpert confirmExpert(long userId) throws ResourceNotFoundException {
        User user = checkUser(userId);
        PetExpert petExpert = expertRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert with user id = " + userId + " not found"));
        Role role_expert = roleRepository.findByName("ROLE_EXPERT");
        List<Role> roles = user.getRoles();
        roles.add(role_expert);
        userRepository.save(user);
        expertRepository.confirmExpert(petExpert.getId());
        return petExpert;
    }

    public PetExpert rejectExpert(long userId) throws ResourceNotFoundException {
        PetExpert petExpert = expertRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert with user id = " + userId + " not found"));
        expertRepository.delete(petExpert);
        return petExpert;
    }

    public PetExpert deleteExpert(long userId) throws ResourceNotFoundException {
        User user = checkUser(userId);
        PetExpert petExpert = expertRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert with user id = " + userId + " not found"));
        Role roleExpert = roleRepository.findByName("ROLE_EXPERT");
        List<Role> roles = user.getRoles();
        roles.remove(roleExpert);
        userRepository.save(user);
        expertRepository.delete(petExpert);
        return petExpert;
    }

    private User checkUser(long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
    }
}
