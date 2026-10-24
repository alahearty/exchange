package com.example.exchange.helper;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * BCrypt password hashing for local accounts. Hashes are salted, adaptive and one-way.
 * The plaintext password is never logged or stored.
 */
public final class PasswordHasher {
    private static final int COST = 12;

    private PasswordHasher() {
    }

    public static String hash(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password is required");
        }
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }

    public static boolean matches(String password, String storedHash) {
        if (password == null || storedHash == null || storedHash.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.verifyer().verify(password.toCharArray(), storedHash).verified;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isHash(String value) {
        return value != null
                && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }

    /**
     * Small administrative utility for generating a hash without printing the password.
     * Usage: java ... PasswordHasher <password>
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: PasswordHasher <password>");
            return;
        }
        System.out.println(hash(args[0]));
    }
}
