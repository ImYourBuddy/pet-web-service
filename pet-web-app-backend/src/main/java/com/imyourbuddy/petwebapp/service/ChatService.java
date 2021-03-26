package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Chat;

import java.util.List;
import java.util.Optional;

/**
 * Service class for {@link Chat}
 */

@Service
public class ChatService {
    public final ChatRepository repository;

    @Autowired
    public ChatService(ChatRepository repository) {
        this.repository = repository;
    }

    public long getChatId(long userId, long expertId) {
        Chat chat = repository.findChatByUserIdAndAndExpertId(userId, expertId);
        if(chat.getId() == 0) {
            Chat newChat = new Chat(userId, expertId);
            repository.save(newChat);
            newChat = repository.findChatByUserIdAndAndExpertId(userId, expertId);
            return newChat.getId();
        }
        return chat.getId();
    }

    public List<Chat> getAllChatByUser(long userId) {
        return repository.findByUser(userId);
    }
}
