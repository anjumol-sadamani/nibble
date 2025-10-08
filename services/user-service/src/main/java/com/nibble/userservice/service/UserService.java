package com.nibble.userservice.service;

public interface UserService {

    void saveOrUpdateUser(String email, String username);

    boolean isExistingUser(String email, String username);
}
