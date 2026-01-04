package com.nibble.auth.config;

public record PermissionDefinition(
    String name,
    String description,
    String category
) {
}
