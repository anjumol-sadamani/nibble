package com.nibble.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String username;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastLogin = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastLogin = LocalDateTime.now();
    }
    
    public User(String email, String username) {
        this.email = email;
        this.username = username;
        this.createdAt = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }
}