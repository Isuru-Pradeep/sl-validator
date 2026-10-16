package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PostalCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"00100", "00700", "20000", "80000", " 10350 "})
    void acceptsFiveDigitCodes(String code) {
        assertTrue(PostalCode.isValid(code));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1000", "100000", "10 350", "1035A", "١٠٣٥٠"})
    void rejectsMalformedCodes(String code) {
        assertFalse(PostalCode.isValid(code));
    }

    @Test
    void normalizeTrimsWhitespace() {
        assertEquals("20000", PostalCode.normalize("  20000 "));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.normalize("2000"));
    }
}
