package com.imyourbuddy.petwebapp.model.projection;

public interface ChatQueryResult {
    long getId();
    long getUser();
    String getUserName();
    long getExpert();
    String getExpertName();

    void setId(long id);
    void setUser(long user);
    void setUserName(String userName);
    void setExpert(long expert);
    void setExpertName(String expertName);
}
