package com.example.exchange;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.exchange.helper.CsrfTokens;
import com.example.exchange.helper.PasswordHasher;

public class PasswordAndCsrfTest {
    @Test
    public void bcryptHashVerifiesOnlyTheCorrectPassword() {
        String hash = PasswordHasher.hash("CorrectHorseBatteryStaple");

        assertTrue(PasswordHasher.isHash(hash));
        assertFalse(hash.contains("CorrectHorseBatteryStaple"));
        assertTrue(PasswordHasher.matches("CorrectHorseBatteryStaple", hash));
        assertFalse(PasswordHasher.matches("wrong", hash));
        assertFalse(PasswordHasher.matches("CorrectHorseBatteryStaple", "not-a-hash"));
    }

    @Test
    public void csrfTokenComparisonRequiresExactMatch() {
        String first = CsrfTokens.newToken();
        String second = CsrfTokens.newToken();

        assertNotEquals(first, second);
        assertTrue(CsrfTokens.isValid(first, first));
        assertFalse(CsrfTokens.isValid(first, second));
        assertFalse(CsrfTokens.isValid(first, null));
        assertFalse(CsrfTokens.isValid(null, first));
    }
}
