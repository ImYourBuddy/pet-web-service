package com.imyourbuddy.petwebapp.dto.response;

import com.imyourbuddy.petwebapp.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private List<String> roles;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public static UserResponse fromUser(User user) {
        UserResponse adminUserDto = new UserResponse();
        adminUserDto.setId(user.getId());
        adminUserDto.setUsername(user.getUsername());
        adminUserDto.setFirstName(user.getFirstName());
        adminUserDto.setLastName(user.getLastName());
        return adminUserDto;
    }
}
