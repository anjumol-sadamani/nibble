package com.nibble.userservice.service;

public interface UserService {
    void save(String email, String username);
    boolean isExistingUser(String email, String username);
}
