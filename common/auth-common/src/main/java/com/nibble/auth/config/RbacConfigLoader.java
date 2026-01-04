package com.nibble.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;

public class RbacConfigLoader {
    
    private static final String CONFIG_FILE = "rbac-config.yml";
    private static RbacConfiguration configuration;
    
    static {
        try {
            loadConfiguration();
        } catch (Exception e) {
            throw new RbacConfigException("Failed to load RBAC configuration", e);
        }
    }

    public static RbacConfig getConfig() {
        return configuration.rbac();
    }
    
    private static void loadConfiguration() {
        InputStream inputStream = RbacConfigLoader.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE);
        
        if (inputStream == null) {
            throw new RbacConfigException("File not found: " + CONFIG_FILE);
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            configuration = mapper.readValue(inputStream, RbacConfiguration.class);
            
            if (configuration == null || configuration.rbac() == null) {
                throw new RbacConfigException("Invalid configuration structure");
            }
        } catch (Exception e) {
            throw new RbacConfigException("Failed to parse YAML configuration", e);
        }
    }
    
    public static boolean isValidPermission(String permissionName) {
        return getConfig().permissions().stream()
                .anyMatch(p -> p.name().equals(permissionName));
    }
    
    public static boolean isValidRole(String roleName) {
        return getConfig().roles().containsKey(roleName);
    }
    
    private RbacConfigLoader() {
    }
}
