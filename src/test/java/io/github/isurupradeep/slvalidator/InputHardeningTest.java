package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InputHardeningTest {

    private static final String ZWSP = "​";
    private static final String BOM = "﻿";
    private static final String NBSP = " ";

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    @ParameterizedTest
    @ValueSource(strings = {"​853400937V", "853400937V﻿", "8534‍00937V", "853400937­V"})
    void nicIgnoresInvisibleCharacters(String nic) {
        assertEquals("853400937V", Nic.parse(nic).getNic());
    }

    @Test
    void phoneIgnoresInvisibleCharactersAndNoBreakSpaces() {
        assertEquals("+94771234567", Phone.toE164(BOM + "077" + NBSP + "123" + ZWSP + "4567"));
    }

    @Test
    void plateAndPostalCodeIgnoreInvisibleCharacters() {
        assertEquals("WP CAB-1234", VehiclePlate.parse("WP" + NBSP + "CAB-1234" + ZWSP).getPlate());
        assertTrue(PostalCode.isValid(BOM + "20000"));
        assertEquals("20000", PostalCode.normalize(ZWSP + "20000"));
    }

    @Test
    void rejectsOverlongInputWithoutTruncating() {
        String padding = repeat(' ', Inputs.MAX_LENGTH);
        assertFalse(Nic.isValid("853400937V" + padding));
        assertFalse(Phone.isValid("0771234567" + padding));
        assertFalse(VehiclePlate.isValid("CAB-1234" + padding));
        assertFalse(PostalCode.isValid("20000" + padding));
        assertNull(Province.fromName("Western" + padding));
        assertNull(Gender.fromText("male" + padding));
    }

    @Test
    void overlongNicGivesClearReason() {
        InvalidNicException e = assertThrows(InvalidNicException.class,
                () -> Nic.parse(repeat('1', Inputs.MAX_LENGTH + 1)));
        assertTrue(e.getMessage().contains("longer than"), e.getMessage());
    }

    @Test
    void acceptsInputAtTheLengthLimit() {
        String value = "853400937V" + repeat(' ', Inputs.MAX_LENGTH - 10);
        assertEquals(Inputs.MAX_LENGTH, value.length());
        assertTrue(Nic.isValid(value));
    }
}
