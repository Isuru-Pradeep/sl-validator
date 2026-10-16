package io.github.isurupradeep.slvalidator;

import java.util.HashMap;
import java.util.Map;

/** Gender encoded in a Sri Lankan NIC (day-of-year value above 500 means female). */
public enum Gender {
    /** Day-of-year value 1-366. */
    MALE,
    /** Day-of-year value 501-866. */
    FEMALE;

    private static final Map<String, Gender> BY_TEXT = new HashMap<>();

    static {
        for (String s : new String[] {"m", "male", "පිරිමි", "පුරුෂ", "ஆண்"}) {
            BY_TEXT.put(Inputs.nameKey(s), MALE);
        }
        for (String s : new String[] {"f", "female", "ගැහැණු", "ස්ත්‍රී", "කාන්තා", "பெண்"}) {
            BY_TEXT.put(Inputs.nameKey(s), FEMALE);
        }
    }

    /**
     * Normalizes free-text gender input from a form, in English, Sinhala or Tamil, e.g.
     * {@code "M"}, {@code "Female"}, {@code "පිරිමි"} or {@code "பெண்"}. Case and surrounding
     * whitespace are ignored.
     *
     * @param text the gender as entered
     * @return the gender, or {@code null} if the text is not recognised
     */
    public static Gender fromText(String text) {
        String key = Inputs.nameKey(text);
        return key == null ? null : BY_TEXT.get(key);
    }
}
