package com.nibble.auth;

import java.util.List;

public record AuthenticatedUser(String userId, String email, String role, List<String> permissions) {
}
