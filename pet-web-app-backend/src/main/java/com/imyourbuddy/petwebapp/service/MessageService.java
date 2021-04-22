package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.projection.ChatQueryResult;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Message;

import java.util.List;

/**
 * Service class for {@link Message} class.
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

    public List<Message> findChatMessages(long firstUser, long secondUser) throws ResourceNotFoundException {

        long chatId = chatService.getChatId(firstUser, secondUser, false);

        return repository.findAllByChatId(chatId);
    }

    public boolean haveNewMessages(long userId) throws ResourceNotFoundException {
        List<ChatQueryResult> allChatByUser = chatService.getAllChatsByUser(userId);
        for (ChatQueryResult chat : allChatByUser) {
            List<Message> newMessages = repository.findAllReceived(chat.getId(), userId);
            if (newMessages.size() != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean haveNewMessagesInChat(long firstUser, long secondUser) throws ResourceNotFoundException {
        long chatId = chatService.getChatId(firstUser, secondUser, false);
        List<Message> newMessages = repository.findAllReceived(chatId, firstUser);
        if(newMessages.size() != 0) {
            return true;
        }
        return false;
    }

    public void markAsDelivered(long userId, long expertId) throws ResourceNotFoundException {
        long chatId = chatService.getChatId(userId, expertId, false);
        repository.markAsDelivered(chatId);
    }
}
