package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.response.ChatResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.service.ChatService;
import com.imyourbuddy.petwebapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/chat")
@CrossOrigin(origins = "*")
public class ChatController {
    private final MessageService messageService;
    private final ChatService chatService;


    @Autowired
    public ChatController(MessageService messageService, ChatService chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public List<Message> getAllMessagesInChat(@PathVariable(name = "senderId") long senderId,
                                @PathVariable(name = "recipientId") long recipientId) throws ResourceNotFoundException {
        return messageService.findChatMessages(senderId, recipientId);
    }

    @GetMapping("/{id}")
    public List<ChatResponse> getAllChatsByUserId(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return chatService.getChatsWithNewMessages(id);
    }

    @GetMapping("/messages/new/{userId}")
    public boolean haveNewMessages(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return messageService.haveNewMessages(userId);
    }

    @GetMapping("/messages/new/{senderId}/{recipientId}")
    public boolean haveNewMessagesInChat(@PathVariable(name = "senderId") long senderId,
                                         @PathVariable(name = "recipientId") long recipientId) throws ResourceNotFoundException {
        return messageService.haveNewMessagesInChat(senderId, recipientId);
    }

    @PatchMapping("/messages/delivered/{userId}/{expertId}")
    public void markAsDelivered(@PathVariable(name = "userId") long userId,
                                         @PathVariable(name = "expertId") long expertId) throws ResourceNotFoundException {
        messageService.markAsDelivered(userId, expertId);
    }
}
