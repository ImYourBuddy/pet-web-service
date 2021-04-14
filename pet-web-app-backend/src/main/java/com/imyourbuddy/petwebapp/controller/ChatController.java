package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.response.ChatResponse;
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
    public List<ChatResponse> getAllChats(@PathVariable(name = "id") long id){
        return chatService.getChatsWithNewMessages(id);
    }

    @GetMapping("/message/new/{userId}")
    public boolean haveNewMessages(@PathVariable(name = "userId") long userId) {
        return messageService.haveNewMessages(userId);
    }

    @GetMapping("/message/new/{userId}/{expertId}")
    public boolean haveNewMessagesInChat(@PathVariable(name = "userId") long userId,
                                         @PathVariable(name = "expertId") long expertId) {
        return messageService.haveNewMessagesInChat(userId, expertId);
    }

    @PatchMapping("/message/delivered/{userId}/{expertId}")
    public void markAsDelivered(@PathVariable(name = "userId") long userId,
                                         @PathVariable(name = "expertId") long expertId) {
        messageService.markAsDelivered(userId, expertId);
    }
}
