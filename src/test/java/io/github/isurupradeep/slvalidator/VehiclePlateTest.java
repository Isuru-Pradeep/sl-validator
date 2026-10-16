package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class VehiclePlateTest {

    @Test
    void parsesThreeLetterPlateWithProvince() {
        VehiclePlateInfo info = VehiclePlate.parse("WP CAB-1234");
        assertEquals(VehiclePlateFormat.LETTERS, info.getFormat());
        assertEquals(Province.WESTERN, info.getProvince());
        assertEquals("CAB", info.getSeries());
        assertEquals("1234", info.getNumber());
        assertEquals("WP CAB-1234", info.getPlate());
    }

    @Test
    void parsesPlateWithoutProvince() {
        VehiclePlateInfo info = VehiclePlate.parse("CAB-1234");
        assertNull(info.getProvince());
        assertEquals("CAB", info.getSeries());
        assertEquals("CAB-1234", info.getPlate());
    }

    @Test
    void parsesOldNumericPlate() {
        VehiclePlateInfo info = VehiclePlate.parse("250-1234");
        assertEquals(VehiclePlateFormat.NUMERIC, info.getFormat());
        assertNull(info.getProvince());
        assertEquals("250", info.getSeries());
        assertEquals("250-1234", info.getPlate());
    }

    @ParameterizedTest
    @CsvSource({
            "wp cab-1234,   WP CAB-1234",
            "WP CAB 1234,   WP CAB-1234",
            "WPCAB1234,     WP CAB-1234",
            "WP-CAB-1234,   WP CAB-1234",
            "'  sp ka 1234 ', SP KA-1234",
            "NCAB1234,      NC AB-1234",
            "KA1234,        KA-1234",
            "'15 - 1234',   15-1234",
            "'1 1234',      1-1234"
    })
    void normalizesFormatting(String input, String canonical) {
        assertEquals(canonical, VehiclePlate.parse(input).getPlate());
    }

    @Test
    void threeLettersStartingWithProvinceCodeIsASeries() {
        VehiclePlateInfo info = VehiclePlate.parse("SGA-1234");
        assertNull(info.getProvince());
        assertEquals("SGA", info.getSeries());
    }

    @ParameterizedTest
    @ValueSource(strings = {"WP", "CP", "SP", "NP", "EP", "NW", "NC", "UP", "SG"})
    void acceptsEveryProvinceCode(String code) {
        assertEquals(Province.fromCode(code), VehiclePlate.parse(code + " KA-1234").getProvince());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "XY CAB-1234",     // unknown province code
            "ABCD-1234",       // four letters without a province
            "C-1234",          // single letter
            "CAB-12345",       // five digits
            "CAB-123",         // three digits
            "CAB-0000",        // number zero
            "15-0000",         // number zero
            "151234",          // numeric plate without separator
            "015-1234",        // leading zero in series
            "1000-1234",       // four digit series
            "WP 15-1234",      // province on numeric plate
            "CAB-12A4",
            "CAB-١٢٣٤"  // non-ASCII digits
    })
    void rejectsInvalidPlates(String plate) {
        assertFalse(VehiclePlate.isValid(plate));
    }

    @Test
    void parseThrowsOnInvalidPlate() {
        assertThrows(IllegalArgumentException.class, () -> VehiclePlate.parse("ABCD-1234"));
    }
}
