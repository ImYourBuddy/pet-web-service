package com.imyourbuddy.petwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imyourbuddy.petwebapp.dto.request.BanRequest;
import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.service.ModeratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class ModeratorControllerTest {
    @MockBean
    private ModeratorService moderatorService;

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
    public void banUserByIdTestWithUnauthorized() throws Exception {
        mvc.perform(post("/rest/moder/users/1/ban"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void banUserByIdTestWithForbidden() throws Exception {
        BanRequest request = new BanRequest(1L, "Test ban");
        mvc.perform(post("/rest/moder/users/1/ban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void banUserByIdTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        BanRequest request = new BanRequest(1L, "Test ban");
        when(moderatorService.banUserById(1L, request.getDescription()))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(post("/rest/moder/users/1/ban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }
    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void banUserByIdTestWithNotFoundWhenIllegalOperationException() throws Exception {
        BanRequest request = new BanRequest(1L, "Test ban");
        when(moderatorService.banUserById(1L, request.getDescription()))
                .thenThrow(new IllegalOperationException("User is already banned"));
        mvc.perform(post("/rest/moder/users/1/ban")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User is already banned")));
    }

    @Test
    public void unbanUserByIdTestWithUnauthorized() throws Exception {
        mvc.perform(delete("/rest/moder/users/1/ban"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void unbanUserByIdTestWithForbidden() throws Exception {
        mvc.perform(delete("/rest/moder/users/1/ban"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void unbanUserByIdTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        when(moderatorService.unbanUserById(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(delete("/rest/moder/users/1/ban"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void unbanUserByIdTestWithNotFoundWhenIllegalOperationException() throws Exception {
        when(moderatorService.unbanUserById(1L))
                .thenThrow(new IllegalOperationException("User isn't banned"));
        mvc.perform(delete("/rest/moder/users/1/ban"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User isn't banned")));
    }

    @Test
    public void getExpertRequestsTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/moder/experts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void getExpertRequestsTestWithForbidden() throws Exception {
        mvc.perform(get("/rest/moder/experts"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void confirmExpertTestWithUnauthorized() throws Exception {
        mvc.perform(patch("/rest/moder/experts/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void confirmExpertTestWithForbidden() throws Exception {
        mvc.perform(patch("/rest/moder/experts/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void confirmExpertTestWithForbiddenWhenResourceNotFoundException() throws Exception {
        when(moderatorService.confirmExpert(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(patch("/rest/moder/experts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void confirmExpertTestWithForbiddenWhenIllegalOperationException() throws Exception {
        when(moderatorService.confirmExpert(1L))
                .thenThrow(new IllegalOperationException("User isn't banned"));
        mvc.perform(patch("/rest/moder/experts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User isn't banned")));
    }

    @Test
    public void rejectExpertTestWithUnauthorized() throws Exception {
        mvc.perform(delete("/rest/moder/experts/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void rejectExpertTestWithForbidden() throws Exception {
        mvc.perform(delete("/rest/moder/experts/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void rejectExpertTestWithForbiddenWhenResourceNotFoundException() throws Exception {
        when(moderatorService.rejectExpert(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(delete("/rest/moder/experts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void rejectExpertTestWithForbiddenWhenIllegalOperationException() throws Exception {
        when(moderatorService.rejectExpert(1L))
                .thenThrow(new IllegalOperationException("User isn't banned"));
        mvc.perform(delete("/rest/moder/experts/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User isn't banned")));
    }

    @Test
    public void deleteExpertTestWithUnauthorized() throws Exception {
        mvc.perform(delete("/rest/moder/experts/1/delete"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void deleteExpertTestWithForbidden() throws Exception {
        mvc.perform(delete("/rest/moder/experts/1/delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATOR")
    public void deleteExpertTestWithForbiddenWhenResourceNotFoundException() throws Exception {
        when(moderatorService.deleteExpert(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(delete("/rest/moder/experts/1/delete"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }
}
