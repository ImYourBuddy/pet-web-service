package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.response.ChatResponse;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.service.ChatService;
import com.imyourbuddy.petwebapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('OWNER')")
    public List<Message> getAllMessagesInChat(@PathVariable(name = "senderId") long senderId,
                                @PathVariable(name = "recipientId") long recipientId) throws ResourceNotFoundException {
        return messageService.findChatMessages(senderId, recipientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public List<ChatResponse> getAllChatsByUserId(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return chatService.getChatsWithNewMessages(id);
    }

    @GetMapping("/messages/{userId}/new")
    @PreAuthorize("hasRole('OWNER')")
    public boolean haveNewMessages(@PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return messageService.haveNewMessages(userId);
    }

    @GetMapping("/messages/{senderId}/{recipientId}/new")
    @PreAuthorize("hasRole('OWNER')")
    public boolean haveNewMessagesInChat(@PathVariable(name = "senderId") long senderId,
                                         @PathVariable(name = "recipientId") long recipientId) throws ResourceNotFoundException {
        return messageService.haveNewMessagesInChat(senderId, recipientId);
    }

    @PatchMapping("/messages/{senderId}/{recipientId}/delivered")
    @PreAuthorize("hasRole('OWNER')")
    public void markAsDelivered(@PathVariable(name = "senderId") long senderId,
                                         @PathVariable(name = "recipientId") long recipientId) throws ResourceNotFoundException {
        messageService.markAsDelivered(senderId, recipientId);
    }
}
