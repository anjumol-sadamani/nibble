package com.nibble.userservice.service;

import com.nibble.userservice.entity.User;
import com.nibble.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public String saveOrUpdate(String email, String username) {
        String message;
        if (isExistingUser(email, username)) {
            message = "Welcome back, " + username + "!";
        } else {
            save(email, username);
            message = "Welcome to Nibble, " + username + "!";
        }
        return message;
    }

    private void save(String email, String username) {
        final User newUser = new User(email, username);
        userRepository.save(newUser);
    }

    private boolean isExistingUser(String email, String username) {
        final Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }
}


