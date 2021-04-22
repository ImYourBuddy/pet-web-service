package com.imyourbuddy.petwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Size(min = 2, max = 255)
    private String title;

    @NotBlank
    @Size(min = 20, max = 255)
    private  String description;

    @NotBlank
    private String text;

    @Min(value = 1)
    private long author;

    @Column(name = "created_date")
    private Date createdDate;

    private long rating;

    private boolean deleted;

    public Post(String title, String description, String text, long author) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.author = author;
    }
}