package com.imyourbuddy.petwebapp.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Moderator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String login;

    private String password;

    @Column(name = "admin_privileges")
    private boolean adminPrivileges;
}
