package com.nibble.auth.config;

import java.util.List;

public record RoleDefinition(
    String description,
    List<String> permissions
) {
}
