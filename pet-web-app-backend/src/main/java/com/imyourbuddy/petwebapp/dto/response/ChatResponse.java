package com.imyourbuddy.petwebapp.dto.response;

import com.imyourbuddy.petwebapp.model.projection.ChatProjection;
import lombok.Data;

@Data
public class ChatResponse {
    private long id;
    private long user;
    private String userName;
    private long expert;
    private String expertName;
    private boolean haveNewMessages;

    public static ChatResponse fromChatProjection(ChatProjection chatProjection) {
        ChatResponse chatResponse = new ChatResponse();

        chatResponse.setId(chatProjection.getId());
        chatResponse.setUser(chatProjection.getUser());
        chatResponse.setUserName(chatProjection.getUserName());
        chatResponse.setExpert(chatProjection.getExpert());
        chatResponse.setExpertName(chatProjection.getExpertName());

        return chatResponse;
    }
}
