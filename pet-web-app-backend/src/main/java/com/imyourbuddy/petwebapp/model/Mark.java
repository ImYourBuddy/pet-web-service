package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
@IdClass(MarkId.class)
public class Mark {

    @Id
    @Column(name = "post_id")
    private long postId;

    @Id
    @Column(name = "user_id")
    private long userId;

    private boolean liked;


}
