package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.service.ChatService;
import com.imyourbuddy.petwebapp.service.MessageService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class ChatControllerTest {
    @MockBean
    private MessageService messageService;

    @MockBean
    private ChatService chatService;

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
    public void getAllMessagesInChatTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/chat/messages/1/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getAllMessagesInChatTestWithNotFound() throws Exception {
        when(messageService.findChatMessages(1L, 2L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(get("/rest/chat/messages/1/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));

    }

    @Test
    public void getAllChatsByUserIdTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/chat/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getAllChatsByUserIdTestWithNotFound() throws Exception {
        when(chatService.getChatsWithNewMessagesFlag(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(get("/rest/chat/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void haveNewMessagesTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/chat/messages/1/new"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void haveNewMessagesTestWithNotFound() throws Exception {
        when(messageService.haveNewMessages(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(get("/rest/chat/messages/1/new"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void haveNewMessagesInChatTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/chat/messages/1/2/new"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void haveNewMessagesInChatTestWithNotFound() throws Exception {
        when(messageService.haveNewMessagesInChat(1L, 2L))
                .thenThrow(new ResourceNotFoundException("User with id = 2 not found"));
        mvc.perform(get("/rest/chat/messages/1/2/new"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 2 not found")));
    }

    @Test
    public void markAsDeliveredTestWithUnauthorized() throws Exception {
        mvc.perform(patch("/rest/chat/messages/1/2/delivered"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void markAsDeliveredTestWithNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User with id = 2 not found"))
                .when(messageService).markAsDelivered(1L, 2L);
        mvc.perform(patch("/rest/chat/messages/1/2/delivered"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 2 not found")));
    }
}
