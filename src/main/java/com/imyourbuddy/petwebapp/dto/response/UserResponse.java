package com.imyourbuddy.petwebapp.dto.response;

import com.imyourbuddy.petwebapp.model.User;
import lombok.Data;

@Data
public class UserResponse {
    private String login;
    private String firstName;
    private String lastName;

    public User toUser() {
        User user = new User();
        user.setUsername(login);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }

    public static UserResponse fromUser(User user) {
        UserResponse adminUserDto = new UserResponse();
        adminUserDto.setLogin(user.getUsername());
        adminUserDto.setFirstName(user.getFirstName());
        adminUserDto.setLastName(user.getLastName());
        return adminUserDto;
    }
}
