package com.imyourbuddy.petwebapp.dto.response;

import com.imyourbuddy.petwebapp.model.projection.ChatQueryResult;
import lombok.Data;

@Data
public class ChatResponse {
    private long id;
    private long user;
    private String userName;
    private long expert;
    private String expertName;
    private boolean haveNewMessages;

    public static ChatResponse fromChatQueryResult(ChatQueryResult chatQueryResult) {
        ChatResponse chatResponse = new ChatResponse();

        chatResponse.setId(chatQueryResult.getId());
        chatResponse.setUser(chatQueryResult.getUser());
        chatResponse.setUserName(chatQueryResult.getUserName());
        chatResponse.setExpert(chatQueryResult.getExpert());
        chatResponse.setExpertName(chatQueryResult.getExpertName());

        return chatResponse;
    }
}
