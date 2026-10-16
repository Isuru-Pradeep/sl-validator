package io.github.isurupradeep.slvalidator;

import java.util.Locale;

/** The nine provinces of Sri Lanka, with the two-letter codes used on vehicle number plates. */
public enum Province {
    /** Western Province. */
    WESTERN("WP", "Western"),
    /** Central Province. */
    CENTRAL("CP", "Central"),
    /** Southern Province. */
    SOUTHERN("SP", "Southern"),
    /** Northern Province. */
    NORTHERN("NP", "Northern"),
    /** Eastern Province. */
    EASTERN("EP", "Eastern"),
    /** North Western Province. */
    NORTH_WESTERN("NW", "North Western"),
    /** North Central Province. */
    NORTH_CENTRAL("NC", "North Central"),
    /** Uva Province. */
    UVA("UP", "Uva"),
    /** Sabaragamuwa Province. */
    SABARAGAMUWA("SG", "Sabaragamuwa");

    private final String code;
    private final String displayName;

    Province(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
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
     * Looks up a province by its two-letter code, ignoring case and surrounding whitespace.
     *
     * @param code the province code, e.g. {@code "wp"}
     * @return the province, or {@code null} if the code is unknown
     */
    public static Province fromCode(String code) {
        if (code == null) {
            return null;
        }
        String c = code.trim().toUpperCase(Locale.ROOT);
        for (Province p : values()) {
            if (p.code.equals(c)) {
                return p;
            }
        }
        return null;
    }
}
