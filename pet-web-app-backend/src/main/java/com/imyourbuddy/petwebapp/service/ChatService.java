package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.response.ChatResponse;
import com.imyourbuddy.petwebapp.model.projection.ChatProjection;
import com.imyourbuddy.petwebapp.repository.ChatRepository;
import com.imyourbuddy.petwebapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.imyourbuddy.petwebapp.model.Chat;

import java.util.LinkedList;
import java.util.List;

/**
 * Service class for {@link Chat}
 */

@Service
public class ChatService {
    public final ChatRepository chatRepository;
    public final MessageRepository messageRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }


    public long getChatId(long userId, long expertId, boolean create) {
        Chat chat = chatRepository.findChatByUserIdAndAndExpertId(userId, expertId);
        if(chat == null) {
            if(create) {
                Chat newChat = new Chat(userId, expertId);
                chatRepository.save(newChat);
                newChat = chatRepository.findChatByUserIdAndAndExpertId(userId, expertId);
                return newChat.getId();
            }
            return 0;
        }
        return chat.getId();
    }

    public List<ChatProjection> getAllChatsByUser(long userId) {
        return chatRepository.findByUser(userId);
    }

    public List<ChatResponse> getChatsWithNewMessages(long userId) {
        List<ChatProjection> chatList = chatRepository.findByUser(userId);
        List<ChatResponse> result = new LinkedList<>();
        chatList.forEach(chat -> {
            ChatResponse chatResponse = ChatResponse.fromChatProjection(chat);
            boolean haveNewMessages = messageRepository.findAllReceived(chat.getId(), userId).size() != 0;
            chatResponse.setHaveNewMessages(haveNewMessages);
            result.add(chatResponse);
        });

        return result;
    }
}
