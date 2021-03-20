package com.imyourbuddy.petwebapp.model.projection;

import java.util.Date;

public interface PostProjection {
    long getId();
    String getTitle();
    String getDescription();
    String getText();
    String getAuthor();
    Date getCreatedDate();
    boolean getDeleted();
}
