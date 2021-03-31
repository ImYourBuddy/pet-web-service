package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.projection.ChatProjection;
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

    public long getChatId(long userId, long expertId, boolean create) {
        Chat chat = repository.findChatByUserIdAndAndExpertId(userId, expertId);
        if(chat == null) {
            if(create) {
                Chat newChat = new Chat(userId, expertId);
                repository.save(newChat);
                newChat = repository.findChatByUserIdAndAndExpertId(userId, expertId);
                return newChat.getId();
            }
            return 0;
        }
        return chat.getId();
    }

    public List<ChatProjection> getAllChatByUser(long userId) {
        return repository.findByUser(userId);
    }
}
