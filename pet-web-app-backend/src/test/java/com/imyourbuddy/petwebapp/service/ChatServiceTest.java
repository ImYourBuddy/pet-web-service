package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.response.ChatResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Chat;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.model.MessageStatus;
import com.imyourbuddy.petwebapp.model.projection.ChatQueryResult;
import com.imyourbuddy.petwebapp.repository.ChatRepository;
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

class ChatServiceTest {
    @InjectMocks
    private ChatService chatService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getChatIdTestWithCreatingChat() throws ResourceNotFoundException {
        Chat chat = new Chat(1L, 2L, 3L);
        when(chatRepository.findChatByUserIdAndExpertId(2L, 3L)).thenReturn(null);
        when(chatRepository.save(new Chat(2L, 3L))).thenReturn(chat);

        chatService.getChatId(2L, 3L, true);

        verify(userService).getById(2L);
        verify(userService).getById(3L);
        verify(chatRepository).findChatByUserIdAndExpertId(2L, 3L);
        verify(chatRepository).save(new Chat(2L, 3L));
    }


    @Test
    public void getChatIdTestWithoutCreatingChat() throws ResourceNotFoundException {
        Chat chat = new Chat(1L, 2L, 3L);
        when(chatRepository.findChatByUserIdAndExpertId(2L, 3L)).thenReturn(null);

        long chatId = chatService.getChatId(2L, 3L, false);

        verify(userService).getById(2L);
        verify(userService).getById(3L);
        verify(chatRepository).findChatByUserIdAndExpertId(2L, 3L);
        assertEquals(0L, chatId);
    }

    @Test
    public void getChatsWithNewMessagesFlagTest() throws ResourceNotFoundException {
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

        when(chatRepository.findByUserId(1L)).thenReturn(chats);
        when(messageRepository.findAllReceived(1L, 1L)).thenReturn(firstChatMessages);
        when(messageRepository.findAllReceived(2L, 1L)).thenReturn(new ArrayList<Message>());

        List<ChatResponse> chatsWithNewMessagesFlag = chatService.getChatsWithNewMessagesFlag(1L);

        verify(userService).getById(1L);
        verify(chatRepository).findByUserId(1L);
        verify(messageRepository).findAllReceived(1L, 1L);
        verify(messageRepository).findAllReceived(2L, 1L);
        assertTrue(chatsWithNewMessagesFlag.get(0).isHaveNewMessages());
        assertFalse(chatsWithNewMessagesFlag.get(1).isHaveNewMessages());


    }



}
