package com.projects.userservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Token {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String value;
    @ManyToOne
    private User user;
    private Date expiresAt; // 30 days
    private boolean isActive;
}
