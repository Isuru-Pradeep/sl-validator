package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class NicTest {

    @Test
    void parsesOldMaleNic() {
        NicInfo info = Nic.parse("853400937V");
        assertEquals(NicFormat.OLD, info.getFormat());
        assertEquals(LocalDate.of(1985, 12, 5), info.getDateOfBirth());
        assertEquals(Gender.MALE, info.getGender());
        assertEquals("093", info.getSerialNumber());
    }

    @Test
    void parsesOldFemaleNic() {
        NicInfo info = Nic.parse("855400937V");
        assertEquals(LocalDate.of(1985, 2, 9), info.getDateOfBirth());
        assertEquals(Gender.FEMALE, info.getGender());
    }

    @Test
    void parsesNewNic() {
        NicInfo info = Nic.parse("200012345678");
        assertEquals(NicFormat.NEW, info.getFormat());
        assertEquals(LocalDate.of(2000, 5, 2), info.getDateOfBirth());
        assertEquals(Gender.MALE, info.getGender());
        assertEquals("4567", info.getSerialNumber());
    }

    @Test
    void acceptsLowerCaseAndWhitespace() {
        assertTrue(Nic.isValid("  853400937v "));
        assertTrue(Nic.isValid("853400937x"));
        assertEquals("853400937V", Nic.parse("853400937v").getNic());
    }

    @Test
    void handlesFeb29Rule() {
        assertEquals(LocalDate.of(1984, 2, 29), Nic.parse("840600000V").getDateOfBirth());
        assertFalse(Nic.isValid("850600000V"), "Feb 29 does not exist in 1985");
        assertEquals(LocalDate.of(1985, 3, 1), Nic.parse("850610000V").getDateOfBirth());
        assertEquals(LocalDate.of(1985, 12, 31), Nic.parse("853660000V").getDateOfBirth());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "85340093V",       // too short
            "8534009370V",     // too long
            "853400937A",      // bad suffix
            "850000000V",      // day 0
            "853670000V",      // day 367
            "855000000V",      // day 500
            "858670000V",      // day 867
            "19853400093",     // 11 digits
            "209912345678",    // future birth date
            "85340O937V"       // letter O instead of zero
    })
    void rejectsInvalidNics(String nic) {
        assertFalse(Nic.isValid(nic));
    }

    @Test
    void parseThrowsWithMessage() {
        InvalidNicException e = assertThrows(InvalidNicException.class, () -> Nic.parse("123"));
        assertNotNull(e.getMessage());
    }

    @Test
    void convertsOldToNew() {
        assertEquals("198534000937", Nic.toNewFormat("853400937V"));
        assertEquals("200012345678", Nic.toNewFormat("200012345678"));
    }

    @Test
    void convertsNewToOld() {
        assertEquals("853400937V", Nic.toOldFormat("198534000937", 'V'));
        assertEquals("853400937X", Nic.toOldFormat("198534000937", 'x'));
    }

    @Test
    void newToOldFailsWhenNotRepresentable() {
        assertThrows(InvalidNicException.class, () -> Nic.toOldFormat("200012345678", 'V'));
        assertThrows(InvalidNicException.class, () -> Nic.toOldFormat("198534010937", 'V'));
    }

    @Test
    void roundTripsConversion() {
        String old = "925671234V";
        assertEquals(old, Nic.toOldFormat(Nic.toNewFormat(old), 'V'));
    }

    @Test
    void calculatesAge() {
        assertEquals(41, Nic.parse("853400937V").getAge(LocalDate.of(2026, 12, 5)));
        assertEquals(40, Nic.parse("853400937V").getAge(LocalDate.of(2026, 12, 4)));
    }

    @Test
    void handlesFemaleDayBoundaries() {
        NicInfo first = Nic.parse("855010000V");
        assertEquals(LocalDate.of(1985, 1, 1), first.getDateOfBirth());
        assertEquals(Gender.FEMALE, first.getGender());
        assertEquals(LocalDate.of(1985, 12, 31), Nic.parse("858660000V").getDateOfBirth());
    }

    @Test
    void appliesFeb29RuleToFemales() {
        assertEquals(LocalDate.of(1984, 2, 29), Nic.parse("845600000V").getDateOfBirth());
        assertFalse(Nic.isValid("855600000V"), "Feb 29 does not exist in 1985");
    }

    @Test
    void year1900IsNotALeapYear() {
        assertFalse(Nic.isValid("000600000V"));
        assertFalse(Nic.isValid("190006000000"));
        assertEquals(LocalDate.of(1900, 1, 1), Nic.parse("190000100000").getDateOfBirth());
    }

    @Test
    void parsesNewFemaleNic() {
        NicInfo info = Nic.parse("199586512345");
        assertEquals(LocalDate.of(1995, 12, 30), info.getDateOfBirth());
        assertEquals(Gender.FEMALE, info.getGender());
    }

    @ParameterizedTest
    @ValueSource(strings = {"000012345678", "189934000937"})
    void rejectsNewNicsBornBefore1900(String nic) {
        InvalidNicException e = assertThrows(InvalidNicException.class, () -> Nic.parse(nic));
        assertTrue(e.getMessage().contains("before 1900"), e.getMessage());
    }

    @Test
    void rejectsNonAsciiDigits() {
        assertFalse(Nic.isValid("８５３４００９３７V"));
    }

    @Test
    void ignoresInnerSpaces() {
        assertEquals("198534000937", Nic.parse("1985 340 00937").getNic());
    }

    @Test
    void convertsFemaleNicsBothWays() {
        assertEquals("198586500937", Nic.toNewFormat("858650937V"));
        assertEquals("858650937V", Nic.toOldFormat("198586500937", 'V'));
    }

    @Test
    void conversionRejectsInvalidInput() {
        assertThrows(InvalidNicException.class, () -> Nic.toNewFormat("123"));
        assertThrows(InvalidNicException.class, () -> Nic.toOldFormat("123", 'V'));
        assertThrows(IllegalArgumentException.class, () -> Nic.toOldFormat("198534000937", 'A'));
    }

    @Test
    void toOldFormatKeepsOldNicUnchanged() {
        assertEquals("853400937X", Nic.toOldFormat("853400937X", 'V'));
    }
}
