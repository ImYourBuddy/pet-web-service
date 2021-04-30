package com.imyourbuddy.petwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imyourbuddy.petwebapp.dto.request.EditUserRequest;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Pet;
import com.imyourbuddy.petwebapp.model.PetGender;
import com.imyourbuddy.petwebapp.model.PetSpecies;
import com.imyourbuddy.petwebapp.service.UserService;
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

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest {
    @MockBean
    private UserService userService;

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
    public void editUserTestWithUnauthorized() throws Exception {
        mvc.perform(put("/rest/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void editUserTestWithNotFound() throws Exception {
        EditUserRequest request = new EditUserRequest("Test", "Testov");
        when(userService.editUser(1L, request))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(put("/rest/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void editUserTestWithBadRequest() throws Exception {
        EditUserRequest request = new EditUserRequest("T", "T");
        mvc.perform(put("/rest/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0]", is("size must be between 2 and 30")))
                .andExpect(jsonPath("$.errors[1]", is("size must be between 2 and 30")));
    }

    @Test
    public void getByIdTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getByIdTestWithNotFound() throws Exception {
        when(userService.getById(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(get("/rest/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void addPetTestWithUnauthorized() throws Exception {
        mvc.perform(post("/rest/user/1/pets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void addPetTestWithNotFound() throws Exception {
        Pet pet = new Pet(1L, PetSpecies.CAT, "Cat", "Ordinal cat", PetGender.MALE, new Date(), 1L);
        when(userService.addPet(1L, pet))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(post("/rest/user/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void addPetTestWithBadRequest() throws Exception {
        Pet pet = new Pet(1L, PetSpecies.CAT, "C", "Ordinal cat", PetGender.MALE, new Date(), 1L);
        mvc.perform(post("/rest/user/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("size must be between 2 and 50")));

    }

    @Test
    public void getAllPetsByUserIdWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/user/1/pets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getAllPetsByUserIdWithNotFound() throws Exception {
        when(userService.getAllPetsByUserId(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(get("/rest/user/1/pets"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void deletePetWithUnauthorized() throws Exception {
        mvc.perform(delete("/rest/user/1/pets/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void deletePetWithNotFound() throws Exception {
        when(userService.deletePet(1L, 1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(delete("/rest/user/1/pets/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void getPetByIdWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/user/1/pets/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getPetByIdWithNotFound() throws Exception {
        when(userService.getPetById(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Pet with id = 1 not found"));
        mvc.perform(get("/rest/user/1/pets/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Pet with id = 1 not found")));
    }

    @Test
    public void editPetTestWithUnauthorized() throws Exception {
        mvc.perform(put("/rest/user/1/pets/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void editPetTestWithNotFound() throws Exception {
        Pet pet = new Pet(1L, PetSpecies.CAT, "Cat", "Ordinal cat", PetGender.MALE, new Date(), 1L);
        when(userService.editPet(1L,1L, pet))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(put("/rest/user/1/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void editPetTestWithBadRequest() throws Exception {
        Pet pet = new Pet(1L, PetSpecies.CAT, "C", "Ordinal cat", PetGender.MALE, new Date(), 1L);
        mvc.perform(put("/rest/user/1/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(pet)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("size must be between 2 and 50")));

    }
}
