package io.github.isurupradeep.slvalidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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

    /** Landline area codes after the leading 0. */
    private static final Set<String> AREA_CODES = unmodifiable(
            "11", "21", "23", "24", "25", "26", "27", "31", "32", "33", "34", "35", "36", "37", "38",
            "41", "45", "47", "51", "52", "54", "55", "57", "63", "65", "66", "67", "81", "91");

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
        if (AREA_CODES.contains(prefix)) {
            return PhoneType.LANDLINE;
        }
        return null;
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

    private static String validNationalNumber(String phone) {
        if (!isValid(phone)) {
            throw new IllegalArgumentException("Not a valid Sri Lankan phone number: " + phone);
        }
        return nationalNumber(phone);
    }

    /** Strips formatting and prefixes, returning the 9-digit national number or null. */
    private static String nationalNumber(String phone) {
        if (phone == null) {
            return null;
        }
        String d = phone.trim().replaceAll("[\\s\\-().]", "");
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

    private static Set<String> unmodifiable(String... values) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }
}
