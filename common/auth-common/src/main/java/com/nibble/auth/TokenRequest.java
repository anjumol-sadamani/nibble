package com.nibble.auth;

import java.util.List;

public record TokenRequest(
    String userId,
    String email,
    String role,
    List<String> permissions
) {
}
