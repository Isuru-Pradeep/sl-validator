package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegionTest {

    @Test
    void hasNineProvincesAndTwentyFiveDistricts() {
        assertEquals(9, Province.values().length);
        assertEquals(25, District.values().length);
    }

    @Test
    void districtsPerProvince() {
        Map<Province, Integer> counts = new EnumMap<>(Province.class);
        for (District d : District.values()) {
            counts.merge(d.getProvince(), 1, Integer::sum);
        }
        assertEquals(3, counts.get(Province.WESTERN));
        assertEquals(3, counts.get(Province.CENTRAL));
        assertEquals(3, counts.get(Province.SOUTHERN));
        assertEquals(5, counts.get(Province.NORTHERN));
        assertEquals(3, counts.get(Province.EASTERN));
        assertEquals(2, counts.get(Province.NORTH_WESTERN));
        assertEquals(2, counts.get(Province.NORTH_CENTRAL));
        assertEquals(2, counts.get(Province.UVA));
        assertEquals(2, counts.get(Province.SABARAGAMUWA));
    }

    @Test
    void looksUpProvinceByCode() {
        assertEquals(Province.NORTH_WESTERN, Province.fromCode("NW"));
        assertEquals(Province.SABARAGAMUWA, Province.fromCode(" sg "));
        assertNull(Province.fromCode("XX"));
        assertNull(Province.fromCode(null));
    }

    @Test
    void exposesDisplayNames() {
        assertEquals("North Central", Province.NORTH_CENTRAL.getDisplayName());
        assertEquals("Nuwara Eliya", District.NUWARA_ELIYA.getDisplayName());
    }
}
