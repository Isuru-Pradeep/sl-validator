package io.github.isurupradeep.slvalidator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The nine provinces of Sri Lanka, with the two-letter codes used on vehicle number plates and
 * their names in English, Sinhala and Tamil.
 */
public enum Province {
    /** Western Province. */
    WESTERN("WP", "Western", "බස්නාහිර", "மேல்"),
    /** Central Province. */
    CENTRAL("CP", "Central", "මධ්‍යම", "மத்திய"),
    /** Southern Province. */
    SOUTHERN("SP", "Southern", "දකුණු", "தென்"),
    /** Northern Province. */
    NORTHERN("NP", "Northern", "උතුරු", "வட"),
    /** Eastern Province. */
    EASTERN("EP", "Eastern", "නැගෙනහිර", "கிழக்கு"),
    /** North Western Province. */
    NORTH_WESTERN("NW", "North Western", "වයඹ", "வடமேல்"),
    /** North Central Province. */
    NORTH_CENTRAL("NC", "North Central", "උතුරු මැද", "வட மத்திய"),
    /** Uva Province. */
    UVA("UP", "Uva", "ඌව", "ஊவா"),
    /** Sabaragamuwa Province. */
    SABARAGAMUWA("SG", "Sabaragamuwa", "සබරගමුව", "சப்ரகமுவ");

    /** Words for "province" that may follow a name, in English, Sinhala and Tamil. */
    private static final String[] SUFFIXES = {"province", "පළාත", "மாகாணம்"};
    private static final Map<String, Province> BY_NAME = new HashMap<>();

    static {
        for (Province p : values()) {
            BY_NAME.put(Inputs.nameKey(p.displayName), p);
            BY_NAME.put(Inputs.nameKey(p.sinhalaName), p);
            BY_NAME.put(Inputs.nameKey(p.tamilName), p);
        }
    }

    private final String code;
    private final String displayName;
    private final String sinhalaName;
    private final String tamilName;

    Province(String code, String displayName, String sinhalaName, String tamilName) {
        this.code = code;
        this.displayName = displayName;
        this.sinhalaName = sinhalaName;
        this.tamilName = tamilName;
    }

    /**
     * The two-letter province code, as printed on vehicle number plates.
     *
     * @return the code, e.g. {@code "WP"}
     */
    public String getCode() { return code; }

    /**
     * The English name of the province, without the word "Province".
     *
     * @return the name, e.g. {@code "North Western"}
     */
    public String getDisplayName() { return displayName; }

    /**
     * The Sinhala name of the province, without the word for "province".
     *
     * @return the name, e.g. {@code "බස්නාහිර"}
     */
    public String getSinhalaName() { return sinhalaName; }

    /**
     * The Tamil name of the province, without the word for "province".
     *
     * @return the name, e.g. {@code "மேல்"}
     */
    public String getTamilName() { return tamilName; }

    /**
     * Looks up a province by its two-letter code, ignoring case and surrounding whitespace.
     *
     * @param code the province code, e.g. {@code "wp"}
     * @return the province, or {@code null} if the code is unknown
     */
    public static Province fromCode(String code) {
        String c = Inputs.clean(code);
        if (c == null) {
            return null;
        }
        c = c.toUpperCase(Locale.ROOT);
        for (Province p : values()) {
            if (p.code.equals(c)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Looks up a province by its English, Sinhala or Tamil name. Case, spacing and a trailing
     * word for "province" are ignored, so {@code "Western"}, {@code "western province"},
     * {@code "බස්නාහිර පළාත"} and {@code "மேல் மாகாணம்"} all match.
     *
     * @param name the province name
     * @return the province, or {@code null} if the name is unknown
     */
    public static Province fromName(String name) {
        String key = Inputs.nameKey(name);
        if (key == null) {
            return null;
        }
        return BY_NAME.get(withoutSuffix(key, SUFFIXES));
    }

    /** Removes the first matching suffix, unless that would leave nothing. */
    static String withoutSuffix(String key, String[] suffixes) {
        for (String s : suffixes) {
            String suffix = Inputs.nameKey(s);
            if (key.endsWith(suffix) && key.length() > suffix.length()) {
                return key.substring(0, key.length() - suffix.length());
            }
        }
        return key;
    }
}
