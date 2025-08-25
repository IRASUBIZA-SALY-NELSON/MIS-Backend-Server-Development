package com.rca.mis;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHashes {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        System.out.println("Generating BCrypt hashes with strength 12:");
        System.out.println();
        
        String[] passwords = {"Admin@123", "Teacher@123", "Student@123"};
        String[] users = {"admin@rca.ac.rw", "teacher@rca.ac.rw", "student@rca.ac.rw"};
        
        for (int i = 0; i < passwords.length; i++) {
            String hash = encoder.encode(passwords[i]);
            System.out.println("User: " + users[i]);
            System.out.println("Password: " + passwords[i]);
            System.out.println("Hash: " + hash);
            System.out.println();
        }
    }
}
