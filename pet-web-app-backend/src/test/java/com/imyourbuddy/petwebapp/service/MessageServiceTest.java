package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.model.MessageStatus;
import com.imyourbuddy.petwebapp.model.projection.ChatQueryResult;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatService chatService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void haveNewMessagesTestWithTrue() throws ResourceNotFoundException {
        ChatQueryResult firstChat = factory.createProjection(ChatQueryResult.class);
        firstChat.setId(1L);
        firstChat.setUser(1L);
        firstChat.setUserName("First user");
        firstChat.setExpert(2L);
        firstChat.setExpertName("Second user");

        ChatQueryResult secondChat = factory.createProjection(ChatQueryResult.class);
        secondChat.setId(2L);
        secondChat.setUser(1L);
        secondChat.setUserName("First user");
        secondChat.setExpert(3L);
        secondChat.setExpertName("Third user");

        List<ChatQueryResult> chats = new ArrayList<>();
        chats.add(firstChat);
        chats.add(secondChat);

        Message message = new Message(1L, 1L, 2L, "Test message", new Date(), MessageStatus.RECEIVED);
        List<Message> firstChatMessages = new ArrayList<>();
        firstChatMessages.add(message);

        when(chatService.getAllChatsByUser(1L)).thenReturn(chats);
        when(messageRepository.findAllReceived(1L, 1L)).thenReturn(firstChatMessages);
        when(messageRepository.findAllReceived(2L, 1L)).thenReturn(new ArrayList<Message>());

        boolean result = messageService.haveNewMessages(1L);

        verify(chatService).getAllChatsByUser(1L);
        verify(messageRepository).findAllReceived(1L, 1L);
        verify(messageRepository, never()).findAllReceived(2L, 1L);
        assertTrue(result);
    }

    @Test
    public void haveNewMessagesTestWithFalse() throws ResourceNotFoundException {

        when(chatService.getAllChatsByUser(1L)).thenReturn(new ArrayList<>());

        boolean result = messageService.haveNewMessages(1L);

        verify(chatService).getAllChatsByUser(1L);
        verify(messageRepository, never()).findAllReceived(1L, 1L);
        verify(messageRepository, never()).findAllReceived(2L, 1L);
        assertFalse(result);
    }

    @Test
    public void haveNewMessagesInChatTestWithTrue() throws ResourceNotFoundException {
        Message message = new Message(1L, 1L, 2L, "Test message", new Date(), MessageStatus.RECEIVED);
        List<Message> chatMessages = new ArrayList<>();
        chatMessages.add(message);

        when(chatService.getChatId(1L, 2L, false)).thenReturn(1L);
        when(messageRepository.findAllReceived(1L, 1L)).thenReturn(chatMessages);

        List<Message> result = messageService.haveNewMessagesInChat(1L, 2L);

        verify(chatService).getChatId(1L, 2L, false);
        verify(messageRepository).findAllReceived(1L, 1L);
        assertTrue(result.size() != 0);

    }

    @Test
    public void haveNewMessagesInChatTestWithFalse() throws ResourceNotFoundException {
        when(chatService.getChatId(1L, 2L, false)).thenReturn(1L);
        when(messageRepository.findAllReceived(1L, 1L)).thenReturn(new ArrayList<>());

        List<Message> result = messageService.haveNewMessagesInChat(1L, 2L);

        verify(chatService).getChatId(1L, 2L, false);
        verify(messageRepository).findAllReceived(1L, 1L);
        assertTrue(result.size() == 0);

    }

}
