package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "0771234567", "077 123 4567", "077-123-4567", "+94771234567",
            "+94 77 123 4567", "0094771234567", "94771234567", "771234567",
            "0701234567", "0711234567", "0721234567", "0741234567",
            "0751234567", "0761234567", "0781234567"
    })
    void acceptsMobileNumbers(String phone) {
        assertTrue(Phone.isValidMobile(phone), phone);
        assertEquals(PhoneType.MOBILE, Phone.typeOf(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0112345678", "(011) 234-5678", "+94112345678", "0812234567", "0912234567"})
    void acceptsLandlines(String phone) {
        assertTrue(Phone.isValidLandline(phone), phone);
        assertFalse(Phone.isValidMobile(phone));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "0731234567",      // unused mobile prefix
            "0791234567",      // unused mobile prefix
            "077123456",       // too short
            "07712345678",     // too long
            "077123456a",      // letter
            "+91771234567",    // wrong country code
            "0991234567",      // unknown area code
            "1234567890"
    })
    void rejectsInvalidNumbers(String phone) {
        assertFalse(Phone.isValid(phone));
        assertNull(Phone.typeOf(phone));
    }

    @Test
    void normalizesToE164() {
        assertEquals("+94771234567", Phone.toE164("077 123 4567"));
        assertEquals("+94112345678", Phone.toE164("011-234-5678"));
    }

    @Test
    void normalizesToLocal() {
        assertEquals("0771234567", Phone.toLocal("+94 77 123 4567"));
        assertEquals("0771234567", Phone.toLocal("0094771234567"));
    }

    @Test
    void formatsForDisplay() {
        assertEquals("077 123 4567", Phone.toNational("+94771234567"));
        assertEquals("011 234 5678", Phone.toNational("0112345678"));
        assertEquals("+94 77 123 4567", Phone.toInternational("0771234567"));
        assertEquals("+94 81 223 4567", Phone.toInternational("081-223-4567"));
        assertThrows(IllegalArgumentException.class, () -> Phone.toNational("0731234567"));
        assertThrows(IllegalArgumentException.class, () -> Phone.toInternational(null));
    }

    @Test
    void normalizeRejectsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Phone.toE164("0731234567"));
        assertThrows(IllegalArgumentException.class, () -> Phone.toLocal("0731234567"));
        assertThrows(IllegalArgumentException.class, () -> Phone.toE164(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "11", "21", "23", "24", "25", "26", "27", "31", "32", "33", "34", "35", "36", "37", "38",
            "41", "45", "47", "51", "52", "54", "55", "57", "63", "65", "66", "67", "81", "91"
    })
    void acceptsEveryAreaCode(String areaCode) {
        assertEquals(PhoneType.LANDLINE, Phone.typeOf("0" + areaCode + "2345678"));
        assertEquals(PhoneType.LANDLINE, Phone.typeOf("+94" + areaCode + "2345678"));
    }

    @ParameterizedTest
    @CsvSource({
            "0112345678,   COLOMBO",
            "0312234567,   GAMPAHA",
            "0362234567,   COLOMBO",
            "+94812234567, KANDY",
            "0512234567,   NUWARA_ELIYA",
            "0672234567,   AMPARA",
            "0912234567,   GALLE"
    })
    void findsDistrictOfLandline(String phone, District district) {
        assertEquals(district, Phone.districtOf(phone));
    }

    @Test
    void everyAreaCodeHasADistrict() {
        String[] codes = {"11", "21", "23", "24", "25", "26", "27", "31", "32", "33", "34", "35", "36",
                "37", "38", "41", "45", "47", "51", "52", "54", "55", "57", "63", "65", "66", "67", "81", "91"};
        for (String code : codes) {
            assertNotNull(Phone.districtOf("0" + code + "2345678"), code);
        }
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"0771234567", "0991234567", "abc"})
    void districtIsNullForMobileAndInvalid(String phone) {
        assertNull(Phone.districtOf(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0094 77 123 4567", "077.123.4567", "  0771234567  ", "+94 (77) 123-4567"})
    void acceptsOtherFormatting(String phone) {
        assertEquals("+94771234567", Phone.toE164(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {"94112345678", "0094112345678", "112345678"})
    void normalizesLandlinePrefixes(String phone) {
        assertEquals("0112345678", Phone.toLocal(phone));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "77١٢٣٤٥٦٧",  // Arabic-Indic digits
            "+94７７１２３４５６７"  // full-width digits
    })
    void rejectsNonAsciiDigits(String phone) {
        assertFalse(Phone.isValid(phone));
    }
}
