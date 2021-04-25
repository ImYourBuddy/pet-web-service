package com.imyourbuddy.petwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Data
@IdClass(MarkId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Mark {

    @Id
    @Column(name = "post_id")
    private long postId;

    @Id
    @Column(name = "user_id")
    private long userId;

    private boolean liked;


}
