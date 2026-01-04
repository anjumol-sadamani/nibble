package com.nibble.userservice.dto;

public record LoginResponse(String token, String email, String username, String role) {
}
