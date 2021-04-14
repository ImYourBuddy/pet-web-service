package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Ban;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.Role;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestProjection;
import com.imyourbuddy.petwebapp.repository.BanRepository;
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
    private final BanRepository banRepository;

    @Autowired
    public ModeratorService(UserRepository userRepository, RoleRepository roleRepository, PetExpertRepository expertRepository, BanRepository banRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.expertRepository = expertRepository;
        this.banRepository = banRepository;
    }

    public User banUserById(BanRequest banRequest) throws ResourceNotFoundException {
        User user = userRepository.findById(banRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + banRequest.getUserId() + " not found"));
        userRepository.banUserById(banRequest.getUserId(), banRequest.isBanned());
        Ban ban = new Ban(banRequest.getUserId(), banRequest.getDescription());
        banRepository.save(ban);
        return user;
    }

    public User unbanUserById(long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
        userRepository.banUserById(userId, false);
        Ban ban = banRepository.findByUserId(userId);
        banRepository.delete(ban);
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

    public void rejectExpert(long expertId) throws ResourceNotFoundException {
        PetExpert petExpert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert with id = " + expertId + " not found"));
        userRepository.findById(petExpert.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + petExpert.getUserId() + " not found"));
        expertRepository.delete(petExpert);
    }

    public void deleteExpert(long userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + userId + " not found"));
        PetExpert petExpert = expertRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Expert with user id = " + userId + " not found"));
        Role roleExpert = roleRepository.findByName("ROLE_EXPERT");
        List<Role> roles = user.getRoles();
        roles.remove(roleExpert);
        user.setRoles(roles);
        userRepository.save(user);
        expertRepository.delete(petExpert);
    }
}
