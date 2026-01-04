package com.nibble.auth;

import com.nibble.auth.config.RbacConfigLoader;
import com.nibble.auth.config.RoleDefinition;

import java.util.Collections;
import java.util.List;

public class PermissionMapper {

    public static List<String> getPermissionsForRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        
        RoleDefinition roleDefinition = RbacConfigLoader.getConfig()
                .roles()
                .get(role.name());
        
        if (roleDefinition == null) {
            throw new IllegalArgumentException("Role not found in RBAC configuration: " + role);
        }
        
        List<String> permissions = roleDefinition.permissions();
        return permissions != null ? Collections.unmodifiableList(permissions) : Collections.emptyList();
    }

    public static String getRoleDescription(Role role) {
        RoleDefinition roleDefinition = RbacConfigLoader.getConfig()
                .roles()
                .get(role.name());
        
        return roleDefinition != null ? roleDefinition.description() : "Unknown role";
    }
    
    private PermissionMapper() {
    }
}
