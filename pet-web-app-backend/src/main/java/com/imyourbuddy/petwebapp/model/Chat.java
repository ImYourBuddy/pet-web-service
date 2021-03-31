package com.imyourbuddy.petwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table
@Data
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    @Min(value = 1)
    private long userId;

    @Column(name = "expert_id")
    @Min(value = 1)
    private long expertId;

    public Chat(long userId, long expertId) {
        this.userId = userId;
        this.expertId = expertId;
    }
}
