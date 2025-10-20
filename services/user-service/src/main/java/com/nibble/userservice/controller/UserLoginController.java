package com.nibble.userservice.controller;

import com.nibble.userservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserLoginController {

    private final UserServiceImpl userService;

    @GetMapping("/")
    public ResponseEntity<String> home(@AuthenticationPrincipal OidcUser user) {
        log.info("User authenticated: {}", user.getEmail());

        String email = user.getEmail();
        String username = user.getGivenName();
        return ResponseEntity.ok(userService.saveOrUpdate(email, username));
    }

}