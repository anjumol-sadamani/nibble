package com.nibble.auth.config;
public class RbacConfigException extends RuntimeException {
    
    public RbacConfigException(String message) {
        super(message);
    }
    
    public RbacConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
