package io.github.isurupradeep.slvalidator;

/** The 25 administrative districts of Sri Lanka and the province each belongs to. */
public enum District {
    /** Colombo, Western Province. */
    COLOMBO("Colombo", Province.WESTERN),
    /** Gampaha, Western Province. */
    GAMPAHA("Gampaha", Province.WESTERN),
    /** Kalutara, Western Province. */
    KALUTARA("Kalutara", Province.WESTERN),
    /** Kandy, Central Province. */
    KANDY("Kandy", Province.CENTRAL),
    /** Matale, Central Province. */
    MATALE("Matale", Province.CENTRAL),
    /** Nuwara Eliya, Central Province. */
    NUWARA_ELIYA("Nuwara Eliya", Province.CENTRAL),
    /** Galle, Southern Province. */
    GALLE("Galle", Province.SOUTHERN),
    /** Matara, Southern Province. */
    MATARA("Matara", Province.SOUTHERN),
    /** Hambantota, Southern Province. */
    HAMBANTOTA("Hambantota", Province.SOUTHERN),
    /** Jaffna, Northern Province. */
    JAFFNA("Jaffna", Province.NORTHERN),
    /** Kilinochchi, Northern Province. */
    KILINOCHCHI("Kilinochchi", Province.NORTHERN),
    /** Mannar, Northern Province. */
    MANNAR("Mannar", Province.NORTHERN),
    /** Vavuniya, Northern Province. */
    VAVUNIYA("Vavuniya", Province.NORTHERN),
    /** Mullaitivu, Northern Province. */
    MULLAITIVU("Mullaitivu", Province.NORTHERN),
    /** Batticaloa, Eastern Province. */
    BATTICALOA("Batticaloa", Province.EASTERN),
    /** Ampara, Eastern Province. */
    AMPARA("Ampara", Province.EASTERN),
    /** Trincomalee, Eastern Province. */
    TRINCOMALEE("Trincomalee", Province.EASTERN),
    /** Kurunegala, North Western Province. */
    KURUNEGALA("Kurunegala", Province.NORTH_WESTERN),
    /** Puttalam, North Western Province. */
    PUTTALAM("Puttalam", Province.NORTH_WESTERN),
    /** Anuradhapura, North Central Province. */
    ANURADHAPURA("Anuradhapura", Province.NORTH_CENTRAL),
    /** Polonnaruwa, North Central Province. */
    POLONNARUWA("Polonnaruwa", Province.NORTH_CENTRAL),
    /** Badulla, Uva Province. */
    BADULLA("Badulla", Province.UVA),
    /** Monaragala, Uva Province. */
    MONARAGALA("Monaragala", Province.UVA),
    /** Ratnapura, Sabaragamuwa Province. */
    RATNAPURA("Ratnapura", Province.SABARAGAMUWA),
    /** Kegalle, Sabaragamuwa Province. */
    KEGALLE("Kegalle", Province.SABARAGAMUWA);

    private final String displayName;
    private final Province province;

    District(String displayName, Province province) {
        this.displayName = displayName;
        this.province = province;
    }

    /**
     * The English name of the district.
     *
     * @return the name, e.g. {@code "Nuwara Eliya"}
     */
    public String getDisplayName() { return displayName; }

    /**
     * The province the district belongs to.
     *
     * @return the province
     */
    public Province getProvince() { return province; }
}
