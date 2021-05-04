package com.imyourbuddy.petwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.service.PetExpertService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class PetExpertControllerTest {

    @MockBean
    private PetExpertService expertService;

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
    public void getAllTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/pet-expert"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getByUserIdTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/pet-expert/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER","EXPERT"})
    public void getByUserIdTestWithNotFound() throws Exception {
        when(expertService.getByUserId(1L))
                .thenThrow(new ResourceNotFoundException("Pet expert with user id = 1 not found"));
        mvc.perform(get("/rest/pet-expert/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Pet expert with user id = 1 not found")));
    }

    @Test
    public void editTestWithUnauthorized() throws Exception {
        mvc.perform(put("/rest/pet-expert/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void editTestWithForbidden() throws Exception {
        PetExpert expert = new PetExpert(1L, "Test qualification for test", true,
                1L, 0L, true);
        mvc.perform(put("/rest/pet-expert/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expert)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void editTestWithBadRequest() throws Exception {
        PetExpert expert = new PetExpert(1L, "Test", true,
                1L, 0L, true);
        mvc.perform(put("/rest/pet-expert/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expert)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("size must be between 20 and 255")));
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void editTestWithNotFound() throws Exception {
        PetExpert expert = new PetExpert(1L, "Test qualification for test", true,
                1L, 0L, true);
        when(expertService.edit(1L, expert))
                .thenThrow(new ResourceNotFoundException("Pet expert with user id = 1 not found"));
        mvc.perform(put("/rest/pet-expert/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expert)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Pet expert with user id = 1 not found")));
    }

    @Test
    public void addTestWithUnauthorized() throws Exception {
        mvc.perform(post("/rest/pet-expert"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "OWNER")
    public void addTestWithBadRequest() throws Exception {
        PetExpert expert = new PetExpert(1L, "Test", true, 1L,
                0L, true);
        mvc.perform(post("/rest/pet-expert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expert)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("size must be between 20 and 255")));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void addTestWithNotFound() throws Exception {
        PetExpert expert = new PetExpert(1L, "Test qualification for test", true, 1L,
                0L, true);
        when(expertService.save(expert))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(post("/rest/pet-expert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expert)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }
}
