package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String title;

    public  String description;

    public String text;

    public long author;
}
