package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.response.ChatResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.projection.ChatQueryResult;
import com.imyourbuddy.petwebapp.repository.ChatRepository;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for {@link Chat} class.
 */

@Service
public class ChatService {
    public final ChatRepository chatRepository;
    public final MessageRepository messageRepository;
    public final UserService userService;

    @Autowired
    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public long getChatId(long firstUser, long secondUser, boolean create) throws ResourceNotFoundException {
        userService.getById(firstUser);
        userService.getById(secondUser);
        Chat chat = chatRepository.findChatByUserIdAndExpertId(firstUser, secondUser);
        if(chat == null) {
            if(create) {
                Chat newChat = new Chat(firstUser, secondUser);
                chatRepository.save(newChat);
                newChat = chatRepository.findChatByUserIdAndExpertId(firstUser, secondUser);
                return newChat.getId();
            } else {
                return 0;
            }
        }
        return chat.getId();
    }

    public List<ChatQueryResult> getAllChatsByUser(long userId) throws ResourceNotFoundException {
        userService.getById(userId);
        return chatRepository.findByUserId(userId);
    }

    public List<ChatResponse> getChatsWithNewMessages(long userId) throws ResourceNotFoundException {
        List<ChatQueryResult> chatList = getAllChatsByUser(userId);
        List<ChatResponse> result = new ArrayList<>();
        chatList.forEach(chat -> {
            ChatResponse chatResponse = ChatResponse.fromChatQueryResult(chat);
            boolean haveNewMessages = messageRepository.findAllReceived(chat.getId(), userId).size() != 0;
            chatResponse.setHaveNewMessages(haveNewMessages);
            result.add(chatResponse);
        });

        return result;
    }
}
