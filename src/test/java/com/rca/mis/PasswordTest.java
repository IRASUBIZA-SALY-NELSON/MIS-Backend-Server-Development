package com.rca.mis;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String password = "Admin@123";
        
        // Hash from seed data
        String seedHash = "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8KzKz6K";
        
        // Test if password matches the seed hash
        boolean matches = encoder.matches(password, seedHash);
        System.out.println("Password 'Admin@123' matches seed hash: " + matches);
        
        // Generate a new hash for comparison
        String newHash = encoder.encode(password);
        System.out.println("Generated new hash: " + newHash);
        
        // Test if password matches the new hash
        boolean newMatches = encoder.matches(password, newHash);
        System.out.println("Password matches new hash: " + newMatches);
    }
}
