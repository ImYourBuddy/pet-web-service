package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Chat;

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
}
