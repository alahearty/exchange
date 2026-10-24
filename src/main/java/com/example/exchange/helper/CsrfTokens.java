package com.example.exchange.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Session synchronizer tokens for state-changing form submissions.
 */
public final class CsrfTokens {
    public static final String SESSION_ATTRIBUTE = "csrfToken";
    public static final String FORM_PARAMETER = "csrfToken";
    public static final String HEADER_NAME = "X-CSRF-Token";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfTokens() {
    }

    public static String newToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static boolean isValid(String expected, String supplied) {
        if (expected == null || supplied == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                supplied.getBytes(StandardCharsets.UTF_8));
    }
}
