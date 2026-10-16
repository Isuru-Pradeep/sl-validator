package io.github.isurupradeep.slvalidator;

import java.util.HashMap;
import java.util.Map;

/**
 * The 25 administrative districts of Sri Lanka, the province each belongs to, and their names
 * in English, Sinhala and Tamil.
 */
public enum District {
    /** Colombo, Western Province. */
    COLOMBO("Colombo", "කොළඹ", "கொழும்பு", Province.WESTERN),
    /** Gampaha, Western Province. */
    GAMPAHA("Gampaha", "ගම්පහ", "கம்பஹா", Province.WESTERN),
    /** Kalutara, Western Province. */
    KALUTARA("Kalutara", "කළුතර", "களுத்துறை", Province.WESTERN),
    /** Kandy, Central Province. */
    KANDY("Kandy", "මහනුවර", "கண்டி", Province.CENTRAL),
    /** Matale, Central Province. */
    MATALE("Matale", "මාතලේ", "மாத்தளை", Province.CENTRAL),
    /** Nuwara Eliya, Central Province. */
    NUWARA_ELIYA("Nuwara Eliya", "නුවරඑළිය", "நுவரெலியா", Province.CENTRAL),
    /** Galle, Southern Province. */
    GALLE("Galle", "ගාල්ල", "காலி", Province.SOUTHERN),
    /** Matara, Southern Province. */
    MATARA("Matara", "මාතර", "மாத்தறை", Province.SOUTHERN),
    /** Hambantota, Southern Province. */
    HAMBANTOTA("Hambantota", "හම්බන්තොට", "அம்பாந்தோட்டை", Province.SOUTHERN),
    /** Jaffna, Northern Province. */
    JAFFNA("Jaffna", "යාපනය", "யாழ்ப்பாணம்", Province.NORTHERN),
    /** Kilinochchi, Northern Province. */
    KILINOCHCHI("Kilinochchi", "කිලිනොච්චිය", "கிளிநொச்சி", Province.NORTHERN),
    /** Mannar, Northern Province. */
    MANNAR("Mannar", "මන්නාරම", "மன்னார்", Province.NORTHERN),
    /** Vavuniya, Northern Province. */
    VAVUNIYA("Vavuniya", "වව්නියාව", "வவுனியா", Province.NORTHERN),
    /** Mullaitivu, Northern Province. */
    MULLAITIVU("Mullaitivu", "මුලතිව්", "முல்லைத்தீவு", Province.NORTHERN),
    /** Batticaloa, Eastern Province. */
    BATTICALOA("Batticaloa", "මඩකලපුව", "மட்டக்களப்பு", Province.EASTERN),
    /** Ampara, Eastern Province. */
    AMPARA("Ampara", "අම්පාර", "அம்பாறை", Province.EASTERN),
    /** Trincomalee, Eastern Province. */
    TRINCOMALEE("Trincomalee", "ත්‍රිකුණාමලය", "திருகோணமலை", Province.EASTERN),
    /** Kurunegala, North Western Province. */
    KURUNEGALA("Kurunegala", "කුරුණෑගල", "குருநாகல்", Province.NORTH_WESTERN),
    /** Puttalam, North Western Province. */
    PUTTALAM("Puttalam", "පුත්තලම", "புத்தளம்", Province.NORTH_WESTERN),
    /** Anuradhapura, North Central Province. */
    ANURADHAPURA("Anuradhapura", "අනුරාධපුරය", "அனுராதபுரம்", Province.NORTH_CENTRAL),
    /** Polonnaruwa, North Central Province. */
    POLONNARUWA("Polonnaruwa", "පොළොන්නරුව", "பொலன்னறுவை", Province.NORTH_CENTRAL),
    /** Badulla, Uva Province. */
    BADULLA("Badulla", "බදුල්ල", "பதுளை", Province.UVA),
    /** Monaragala, Uva Province. */
    MONARAGALA("Monaragala", "මොණරාගල", "மொனராகலை", Province.UVA),
    /** Ratnapura, Sabaragamuwa Province. */
    RATNAPURA("Ratnapura", "රත්නපුර", "இரத்தினபுரி", Province.SABARAGAMUWA),
    /** Kegalle, Sabaragamuwa Province. */
    KEGALLE("Kegalle", "කෑගල්ල", "கேகாலை", Province.SABARAGAMUWA);

    /** Words for "district" that may follow a name, in English, Sinhala and Tamil. */
    private static final String[] SUFFIXES = {"district", "දිස්ත්‍රික්කය", "மாவட்டம்"};
    private static final Map<String, District> BY_NAME = new HashMap<>();

    static {
        for (District d : values()) {
            BY_NAME.put(Inputs.nameKey(d.displayName), d);
            BY_NAME.put(Inputs.nameKey(d.sinhalaName), d);
            BY_NAME.put(Inputs.nameKey(d.tamilName), d);
        }
    }

    private final String displayName;
    private final String sinhalaName;
    private final String tamilName;
    private final Province province;

    District(String displayName, String sinhalaName, String tamilName, Province province) {
        this.displayName = displayName;
        this.sinhalaName = sinhalaName;
        this.tamilName = tamilName;
        this.province = province;
    }

    /**
     * The English name of the district.
     *
     * @return the name, e.g. {@code "Nuwara Eliya"}
     */
    public String getDisplayName() { return displayName; }

    /**
     * The Sinhala name of the district.
     *
     * @return the name, e.g. {@code "කොළඹ"}
     */
    public String getSinhalaName() { return sinhalaName; }

    /**
     * The Tamil name of the district.
     *
     * @return the name, e.g. {@code "கொழும்பு"}
     */
    public String getTamilName() { return tamilName; }

    /**
     * The province the district belongs to.
     *
     * @return the province
     */
    public Province getProvince() { return province; }

    /**
     * Looks up a district by its English, Sinhala or Tamil name. Case, spacing and a trailing
     * word for "district" are ignored, so {@code "Nuwara Eliya"}, {@code "nuwaraeliya district"},
     * {@code "නුවර එළිය"} and {@code "நுவரெலியா"} all match.
     *
     * @param name the district name
     * @return the district, or {@code null} if the name is unknown
     */
    public static District fromName(String name) {
        String key = Inputs.nameKey(name);
        if (key == null) {
            return null;
        }
        return BY_NAME.get(Province.withoutSuffix(key, SUFFIXES));
    }
}
