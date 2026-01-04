package com.nibble.userservice.service;

import com.nibble.auth.JwtService;
import com.nibble.auth.PermissionMapper;
import com.nibble.auth.TokenRequest;
import com.nibble.userservice.dto.LoginResponse;
import com.nibble.userservice.entity.User;
import com.nibble.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void save(String email, String username) {
        final User newUser = new User(email, username);
        userRepository.save(newUser);
    }

    @Override
    public boolean isExistingUser(String email, String username) {
        final Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }
    
    public LoginResponse generateLoginResponse(String email, String username) {
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User(email, username);
                    return userRepository.save(newUser);
                });
        
        List<String> permissions = PermissionMapper.getPermissionsForRole(user.getRole());
        
        TokenRequest tokenRequest = new TokenRequest(
                user.getId().toString(),
                user.getEmail(),
                user.getRole().name(),
                permissions
        );
        
        String token = jwtService.generateToken(tokenRequest);
        
        return new LoginResponse(token, user.getEmail(), user.getUsername(), user.getRole().name());
    }
}
