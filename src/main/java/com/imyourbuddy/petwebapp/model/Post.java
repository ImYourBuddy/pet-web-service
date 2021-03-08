package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private  String description;

    private String text;

    private long author;
}
