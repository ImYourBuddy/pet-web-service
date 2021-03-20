package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "owner_id")
    @Min(value = 1)
    private long ownerId;

    @Column(name = "expert_id")
    @Min(value = 1)
    private long expertId;

    private boolean deleted;


}
