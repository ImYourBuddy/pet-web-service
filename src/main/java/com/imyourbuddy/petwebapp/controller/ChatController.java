package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.service.ChatService;
import com.imyourbuddy.petwebapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/chat")
public class ChatController {
    public final ChatService chatService;
    public final MessageService messageService;

    @Autowired
    public ChatController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }
}
