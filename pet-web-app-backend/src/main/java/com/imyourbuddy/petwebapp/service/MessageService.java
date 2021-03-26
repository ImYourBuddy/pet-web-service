package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for {@link Message}
 */

@Service
public class MessageService {
    private final MessageRepository repository;
    private final ChatService chatService;

    @Autowired
    public MessageService(MessageRepository repository, ChatService chatService) {
        this.repository = repository;
        this.chatService = chatService;
    }

    public Message save(Message chatMessage) {
        return repository.save(chatMessage);
    }

    public List<Message> findChatMessages(long userId, long expertId) {
        long chatId = chatService.getChatId(userId, expertId);

        List<Message> messages = repository.findAllByChatId(chatId);

        return messages;
    }
}
