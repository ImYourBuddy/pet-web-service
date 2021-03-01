package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.Moderator;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Message;

/**
 * Service class for {@link Message}
 */

@Service
public class MessageService {
    public final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }
}
