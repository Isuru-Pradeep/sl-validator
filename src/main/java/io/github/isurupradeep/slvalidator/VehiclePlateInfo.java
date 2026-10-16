package io.github.isurupradeep.slvalidator;

/** Immutable result of parsing a valid vehicle number plate. */
public final class VehiclePlateInfo {
    private final VehiclePlateFormat format;
    private final Province province;
    private final String series;
    private final String number;

    VehiclePlateInfo(VehiclePlateFormat format, Province province, String series, String number) {
        this.format = format;
        this.province = province;
        this.series = series;
        this.number = number;
    }

    /**
     * The plate in canonical form, e.g. {@code "WP CAB-1234"}, {@code "CAB-1234"} or {@code "15-1234"}.
     *
     * @return the canonical plate
     */
    public String getPlate() {
        String plate = series + "-" + number;
        return province == null ? plate : province.getCode() + " " + plate;
    }

    /**
     * The format of the plate.
     *
     * @return whether the plate uses a numeric or letter series
     */
    public VehiclePlateFormat getFormat() { return format; }

    /**
     * The province printed on the plate. Newer plates and old numeric plates carry no province.
     *
     * @return the province, or {@code null} if the plate has none
     */
    public Province getProvince() { return province; }

    /**
     * The series before the number.
     *
     * @return the letters, e.g. {@code "CAB"}, or the numeric series, e.g. {@code "15"}
     */
    public String getSeries() { return series; }

    /**
     * The four-digit number.
     *
     * @return the number, e.g. {@code "1234"}
     */
    public String getNumber() { return number; }

    @Override
    public String toString() {
        return "VehiclePlateInfo{plate=" + getPlate() + ", format=" + format + ", province=" + province + "}";
    }
}
