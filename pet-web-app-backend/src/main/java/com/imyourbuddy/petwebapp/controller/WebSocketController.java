package com.imyourbuddy.petwebapp.controller;


import com.imyourbuddy.petwebapp.dto.response.ChatMessage;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Message;
import com.imyourbuddy.petwebapp.model.MessageStatus;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.service.ChatService;
import com.imyourbuddy.petwebapp.service.MessageService;
import com.imyourbuddy.petwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.validation.Valid;


@Controller
@CrossOrigin(origins = "*")
public class WebSocketController {
    private final ChatService chatService;
    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(ChatService chatService, MessageService messageService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send/message/{to}")
    public void sendMessage(@DestinationVariable long to,@Payload @Valid Message message) throws ResourceNotFoundException {
        userService.getById(to);
        long chatId = chatService.getChatId(message.getSender(), to, true);
        message.setChatId(chatId);
        message.setStatus(MessageStatus.RECEIVED);
        messageService.save(message);
        User user = userService.getById(message.getSender());
        String senderName = user.getFirstName() + " " + user.getLastName();
        ChatMessage chatMessage = new ChatMessage(message.getChatId(), message.getSender(),
                senderName, message.getText(), message.getTimestamp());
        this.messagingTemplate.convertAndSend("/chat/" + to, chatMessage);
    }
}
