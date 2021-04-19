package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chat_id")
    private long chatId;

    @Min(value = 1)
    private long sender;

    @NotBlank
    private String text;

    @Column
    private Date timestamp;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;





}
