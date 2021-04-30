package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.security.UserDetailsServiceImpl;
import com.imyourbuddy.petwebapp.service.AdminService;
import com.imyourbuddy.petwebapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AdminControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private AdminService adminService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getAllUsersTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void getAllUsersTestWithForbidden() throws Exception {
        mvc.perform(get("/rest/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserByIdTestWithUnauthorized() throws Exception {
        mvc.perform(patch("/rest/admin/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void deleteUserByIdTestWithForbidden() throws Exception {
        mvc.perform(patch("/rest/admin/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void deleteUserByIdTestWithNotFound() throws Exception {
        when(userService.deleteUserById(1L)).thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(patch("/rest/admin/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void addModerByUserIdTestWithUnauthorized() throws Exception {
        mvc.perform(post("/rest/admin/users/1/moder"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void addModerByUserIdTestWithForbidden() throws Exception {
        mvc.perform(post("/rest/admin/users/1/moder"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void addModerByUserIdTestWithNotFound() throws Exception {
        when(adminService.addModerByUserId(1L)).thenThrow(new ResourceNotFoundException("User with id = 1 not found."));
        mvc.perform(post("/rest/admin/users/1/moder"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found.")));
    }

    @Test
    public void removeModerByUserIdTestWithUnauthorized() throws Exception {
        mvc.perform(delete("/rest/admin/users/1/moder"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void removeModerByUserIdTestWithForbidden() throws Exception {
        mvc.perform(delete("/rest/admin/users/1/moder"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void removeModerByUserIdTestWithNotFound() throws Exception {
        when(adminService.removeModerByUserId(1L)).thenThrow(new ResourceNotFoundException("User with id = 1 not found."));
        mvc.perform(delete("/rest/admin/users/1/moder"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found.")));
    }
}
