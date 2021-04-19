package com.imyourbuddy.petwebapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "post_image")
@Data
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "post_id")
    private long postId;

    private byte[] image;

    public PostImage(long postId, byte[] image) {
        this.postId = postId;
        this.image = image;
    }
}
