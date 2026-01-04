package com.nibble.auth.config;

import java.util.List;
import java.util.Map;

public record RbacConfig(
    String version,
    List<PermissionDefinition> permissions,
    Map<String, RoleDefinition> roles
) {
}
