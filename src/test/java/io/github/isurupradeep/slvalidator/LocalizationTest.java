package io.github.isurupradeep.slvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationTest {

    @ParameterizedTest
    @ValueSource(strings = {"Western", "western province", "WESTERN PROVINCE", "බස්නාහිර", "බස්නාහිර පළාත",
            "மேல்", "மேல் மாகாணம்"})
    void findsProvinceByNameInAnyLanguage(String name) {
        assertEquals(Province.WESTERN, Province.fromName(name));
    }

    @Test
    void distinguishesSimilarProvinceNames() {
        assertEquals(Province.NORTHERN, Province.fromName("උතුරු"));
        assertEquals(Province.NORTH_CENTRAL, Province.fromName("උතුරු මැද"));
        assertEquals(Province.NORTHERN, Province.fromName("வட"));
        assertEquals(Province.NORTH_WESTERN, Province.fromName("வடமேல்"));
        assertEquals(Province.NORTH_CENTRAL, Province.fromName("வட மத்திய"));
        assertEquals(Province.CENTRAL, Province.fromName("Central"));
    }

    @Test
    void sinhalaMatchesWithOrWithoutZeroWidthJoiner() {
        assertEquals(Province.CENTRAL, Province.fromName("මධ්‍යම"));
        assertEquals(Province.CENTRAL, Province.fromName("මධ්යම"));
        assertEquals(District.TRINCOMALEE, District.fromName("ත්‍රිකුණාමලය"));
        assertEquals(District.TRINCOMALEE, District.fromName("ත්රිකුණාමලය"));
        assertEquals(District.KANDY, District.fromName("මහනුවර දිස්ත්රික්කය"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Nuwara Eliya", "nuwaraeliya", "Nuwara Eliya District", "නුවරඑළිය", "නුවර එළිය",
            "நுவரெலியா", "நுவரெலியா மாவட்டம்"})
    void findsDistrictByNameInAnyLanguage(String name) {
        assertEquals(District.NUWARA_ELIYA, District.fromName(name));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Atlantis", "province", "district", "WP"})
    void unknownNamesReturnNull(String name) {
        assertNull(Province.fromName(name));
        assertNull(District.fromName(name));
    }

    @Test
    void everyNameIsUniqueAndRoundTrips() {
        Set<String> seen = new HashSet<>();
        for (Province p : Province.values()) {
            for (String n : new String[] {p.getDisplayName(), p.getSinhalaName(), p.getTamilName()}) {
                assertTrue(seen.add(Inputs.nameKey(n)), "duplicate " + n);
                assertEquals(p, Province.fromName(n));
            }
        }
        seen.clear();
        for (District d : District.values()) {
            for (String n : new String[] {d.getDisplayName(), d.getSinhalaName(), d.getTamilName()}) {
                assertTrue(seen.add(Inputs.nameKey(n)), "duplicate " + n);
                assertEquals(d, District.fromName(n));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"M", "male", " Male ", "පිරිමි", "පුරුෂ", "ஆண்"})
    void normalizesMale(String text) {
        assertEquals(Gender.MALE, Gender.fromText(text));
    }

    @ParameterizedTest
    @ValueSource(strings = {"F", "FEMALE", "ගැහැණු", "ස්ත්‍රී", "ස්ත්රී", "කාන්තා", "பெண்"})
    void normalizesFemale(String text) {
        assertEquals(Gender.FEMALE, Gender.fromText(text));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"x", "other", "mf"})
    void unknownGenderReturnsNull(String text) {
        assertNull(Gender.fromText(text));
    }
}
