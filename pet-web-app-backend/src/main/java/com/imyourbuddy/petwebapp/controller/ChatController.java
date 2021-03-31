package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.Chat;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.model.projection.ChatProjection;
import com.imyourbuddy.petwebapp.service.ChatService;
import com.imyourbuddy.petwebapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class ChatController {
    private final MessageService messageService;
    private final ChatService chatService;


    @Autowired
    public ChatController(MessageService messageService, ChatService chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
    }

    @GetMapping("/message/{senderId}/{recipientId}")
    public List<Message> getAll(@PathVariable(name = "senderId") long senderId,
                                @PathVariable(name = "recipientId") long recipientId) {
        return messageService.findChatMessages(senderId, recipientId);
    }

    @GetMapping("/chat/{id}")
    public List<ChatProjection> getAllChats(@PathVariable(name = "id") long id){
        return chatService.getAllChatByUser(id);
    }
}
