package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.projection.ChatProjection;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Message;

import java.util.List;

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
        long chatId = chatService.getChatId(userId, expertId, false);

        List<Message> messages = repository.findAllByChatId(chatId);

        return messages;
    }

    public List<Message> getAll() {
        return repository.findAll();
    }

    public boolean haveNewMessages(long userId) {
        boolean result = false;
        List<ChatProjection> allChatByUser = chatService.getAllChatsByUser(userId);
        for (ChatProjection chat : allChatByUser) {
            List<Message> newMessages = repository.findAllReceived(chat.getId(), userId);
            if (newMessages.size() != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean haveNewMessagesInChat(long userId, long expertId) {
        long chatId = chatService.getChatId(userId, expertId, false);
        List<Message> newMessages = repository.findAllReceived(chatId, userId);
        if(newMessages.size() != 0) {
            return true;
        }
        return false;
    }

    public void markAsDelivered(long userId, long expertId) {
        long chatId = chatService.getChatId(userId, expertId, false);
        repository.markAsDelivered(chatId);
    }
}
