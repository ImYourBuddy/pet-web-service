package com.imyourbuddy.petwebapp.model.projection;

import lombok.Data;

import java.util.Date;


public interface PostQueryResult {
    long getId();
    String getTitle();
    String getDescription();
    String getText();
    String getAuthor();
    long getRating();
    Date getCreatedDate();
    boolean getDeleted();

    void setId(long id);
    void setTitle(String title);
    void setDescription(String description);
    void setText(String text);
    void setAuthor(String author);
    void setRating(long rating);
    void setCreatedDate(Date createdDate);
    void setDeleted(boolean deleted);
}
