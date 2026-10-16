package io.github.isurupradeep.slvalidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Validation and normalization of Sri Lankan phone numbers.
 *
 * <p>Accepts local ({@code 0771234567}), international ({@code +94771234567}, {@code 0094...},
 * {@code 94...}) and bare 9-digit ({@code 771234567}) forms. Spaces, dashes, dots and
 * parentheses are ignored.</p>
 */
public final class Phone {

    /** Mobile prefixes after the leading 0 (070-078, excluding 073). */
    private static final Set<String> MOBILE_PREFIXES = unmodifiable(
            "70", "71", "72", "74", "75", "76", "77", "78");

    /** Landline area codes after the leading 0, mapped to the district of the exchange town. */
    private static final Map<String, District> AREA_CODES = areaCodes();

    private Phone() {
    }

    /**
     * Checks whether a value is a valid Sri Lankan mobile or landline number.
     *
     * @param phone the phone number in any accepted form
     * @return true for any valid mobile or landline number
     */
    public static boolean isValid(String phone) {
        return typeOf(phone) != null;
    }

    /**
     * Checks whether a value is a valid Sri Lankan mobile number.
     *
     * @param phone the phone number in any accepted form
     * @return true if the number is a valid mobile number
     */
    public static boolean isValidMobile(String phone) {
        return typeOf(phone) == PhoneType.MOBILE;
    }

    /**
     * Checks whether a value is a valid Sri Lankan landline number.
     *
     * @param phone the phone number in any accepted form
     * @return true if the number is a valid landline number
     */
    public static boolean isValidLandline(String phone) {
        return typeOf(phone) == PhoneType.LANDLINE;
    }

    /**
     * Detects the kind of a phone number.
     *
     * @param phone the phone number in any accepted form
     * @return the number type, or {@code null} if the number is invalid
     */
    public static PhoneType typeOf(String phone) {
        String n = nationalNumber(phone);
        if (n == null) {
            return null;
        }
        String prefix = n.substring(0, 2);
        if (MOBILE_PREFIXES.contains(prefix)) {
            return PhoneType.MOBILE;
        }
        if (AREA_CODES.containsKey(prefix)) {
            return PhoneType.LANDLINE;
        }
        return null;
    }

    /**
     * Returns the district of a landline number's area code, e.g. {@code "0812234567" -> KANDY}.
     *
     * <p>Area codes belong to exchange towns, and an exchange can serve neighbouring areas
     * across a district border, so treat the result as approximate. Mobile numbers are not
     * tied to a place and return {@code null}.</p>
     *
     * @param phone the phone number in any accepted form
     * @return the district, or {@code null} if the number is not a valid landline
     */
    public static District districtOf(String phone) {
        if (typeOf(phone) != PhoneType.LANDLINE) {
            return null;
        }
        return AREA_CODES.get(nationalNumber(phone).substring(0, 2));
    }

    /**
     * Normalizes to E.164, e.g. {@code "077 123 4567" -> "+94771234567"}.
     *
     * @param phone the phone number in any accepted form
     * @return the number in E.164 form
     * @throws IllegalArgumentException if the number is invalid
     */
    public static String toE164(String phone) {
        return "+94" + validNationalNumber(phone);
    }

    /**
     * Normalizes to the local 10-digit form, e.g. {@code "+94771234567" -> "0771234567"}.
     *
     * @param phone the phone number in any accepted form
     * @return the number in local form
     * @throws IllegalArgumentException if the number is invalid
     */
    public static String toLocal(String phone) {
        return "0" + validNationalNumber(phone);
    }

    /**
     * Formats for display in Sri Lanka, e.g. {@code "+94771234567" -> "077 123 4567"}.
     *
     * @param phone the phone number in any accepted form
     * @return the number grouped as {@code 0XX XXX XXXX}
     * @throws IllegalArgumentException if the number is invalid
     */
    public static String toNational(String phone) {
        return "0" + grouped(validNationalNumber(phone));
    }

    /**
     * Formats for display abroad, e.g. {@code "0771234567" -> "+94 77 123 4567"}.
     *
     * @param phone the phone number in any accepted form
     * @return the number grouped as {@code +94 XX XXX XXXX}
     * @throws IllegalArgumentException if the number is invalid
     */
    public static String toInternational(String phone) {
        return "+94 " + grouped(validNationalNumber(phone));
    }

    private static String grouped(String n) {
        return n.substring(0, 2) + " " + n.substring(2, 5) + " " + n.substring(5);
    }

    private static String validNationalNumber(String phone) {
        if (!isValid(phone)) {
            throw new IllegalArgumentException("Not a valid Sri Lankan phone number: " + phone);
        }
        return nationalNumber(phone);
    }

    /** Strips formatting and prefixes, returning the 9-digit national number or null. */
    private static String nationalNumber(String phone) {
        String cleaned = Inputs.clean(phone);
        if (cleaned == null) {
            return null;
        }
        String d = cleaned.replaceAll("[\\s\\-().]", "");
        if (d.startsWith("+94")) {
            d = d.substring(3);
        } else if (d.startsWith("0094")) {
            d = d.substring(4);
        } else if (d.startsWith("94") && d.length() == 11) {
            d = d.substring(2);
        } else if (d.startsWith("0") && d.length() == 10) {
            d = d.substring(1);
        }
        if (d.length() != 9 || !d.chars().allMatch(c -> c >= '0' && c <= '9')) {
            return null;
        }
        return d;
    }

    private static Map<String, District> areaCodes() {
        Map<String, District> m = new HashMap<>();
        m.put("11", District.COLOMBO);       // Colombo
        m.put("21", District.JAFFNA);        // Jaffna
        m.put("23", District.MANNAR);        // Mannar
        m.put("24", District.VAVUNIYA);      // Vavuniya
        m.put("25", District.ANURADHAPURA);  // Anuradhapura
        m.put("26", District.TRINCOMALEE);   // Trincomalee
        m.put("27", District.POLONNARUWA);   // Polonnaruwa
        m.put("31", District.GAMPAHA);       // Negombo
        m.put("32", District.PUTTALAM);      // Chilaw
        m.put("33", District.GAMPAHA);       // Gampaha
        m.put("34", District.KALUTARA);      // Kalutara
        m.put("35", District.KEGALLE);       // Kegalle
        m.put("36", District.COLOMBO);       // Avissawella
        m.put("37", District.KURUNEGALA);    // Kurunegala
        m.put("38", District.KALUTARA);      // Panadura
        m.put("41", District.MATARA);        // Matara
        m.put("45", District.RATNAPURA);     // Ratnapura
        m.put("47", District.HAMBANTOTA);    // Hambantota
        m.put("51", District.NUWARA_ELIYA);  // Hatton
        m.put("52", District.NUWARA_ELIYA);  // Nuwara Eliya
        m.put("54", District.KANDY);         // Nawalapitiya
        m.put("55", District.BADULLA);       // Badulla
        m.put("57", District.BADULLA);       // Bandarawela
        m.put("63", District.AMPARA);        // Ampara
        m.put("65", District.BATTICALOA);    // Batticaloa
        m.put("66", District.MATALE);        // Matale
        m.put("67", District.AMPARA);        // Kalmunai
        m.put("81", District.KANDY);         // Kandy
        m.put("91", District.GALLE);         // Galle
        return Collections.unmodifiableMap(m);
    }

    private static Set<String> unmodifiable(String... values) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }
}
