package io.github.isurupradeep.slvalidator;

import java.util.regex.Pattern;

/**
 * Validation of Sri Lankan postal codes.
 *
 * <p>Postal codes are five digits, e.g. {@code 20000} (Kandy) or {@code 00700} (Colombo 07).
 * Only the format is checked: whether a code is actually assigned to a post office is not
 * verified.</p>
 */
public final class PostalCode {

    private static final Pattern CODE = Pattern.compile("^[0-9]{5}$");

    private PostalCode() {
    }

    /**
     * Checks whether a value is a well-formed postal code, ignoring surrounding whitespace.
     *
     * @param code the postal code
     * @return true if the value is exactly five digits
     */
    public static boolean isValid(String code) {
        return code != null && CODE.matcher(code.trim()).matches();
    }

    /**
     * Returns the postal code without surrounding whitespace.
     *
     * @param code the postal code
     * @return the five-digit code
     * @throws IllegalArgumentException if the value is not a well-formed postal code
     */
    public static String normalize(String code) {
        if (!isValid(code)) {
            throw new IllegalArgumentException("Not a valid Sri Lankan postal code: " + code);
        }
        return code.trim();
    }
}
