package io.github.isurupradeep.slvalidator;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/** Shared clean-up of untrusted input before it is validated. */
final class Inputs {

    /** Longer inputs are rejected outright rather than truncated. */
    static final int MAX_LENGTH = 1024;

    /** Zero-width and soft-hyphen characters that are often pasted in invisibly. */
    private static final Pattern INVISIBLE = Pattern.compile("[\\u200B\\u200C\\u200D\\u2060\\uFEFF\\u00AD]");
    /** No-break spaces, treated as ordinary spaces. */
    private static final Pattern NO_BREAK_SPACE = Pattern.compile("[\\u00A0\\u2007\\u202F]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    private Inputs() {
    }

    /**
     * Removes invisible characters, converts no-break spaces and trims.
     *
     * @return the cleaned value, or {@code null} if the input is null or too long
     */
    static String clean(String value) {
        if (value == null || value.length() > MAX_LENGTH) {
            return null;
        }
        String v = INVISIBLE.matcher(value).replaceAll("");
        return NO_BREAK_SPACE.matcher(v).replaceAll(" ").trim();
    }

    /**
     * Builds a comparison key for names in English, Sinhala or Tamil: cleaned, NFC-normalized,
     * without whitespace and lower-cased. Sinhala conjuncts typed with or without a zero-width
     * joiner produce the same key.
     *
     * @return the key, or {@code null} if the input is null or too long
     */
    static String nameKey(String value) {
        String v = clean(value);
        if (v == null) {
            return null;
        }
        v = Normalizer.normalize(v, Normalizer.Form.NFC);
        return WHITESPACE.matcher(v).replaceAll("").toLowerCase(Locale.ROOT);
    }
}
