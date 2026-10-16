package io.github.isurupradeep.slvalidator;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validation and parsing of Sri Lankan vehicle number plates.
 *
 * <p>Accepts letter-series plates ({@code CAB-1234}, {@code KA 1234}), optionally prefixed by a
 * province code ({@code WP CAB-1234}), and old numeric plates ({@code 15-1234}). Input is
 * case-insensitive and the separator may be a hyphen or space; letter-series plates may also
 * omit it. Old plates written with the Sinhala "Sri" letter are not supported.</p>
 *
 * <p>Validation is structural: it cannot tell whether a plate has actually been issued.</p>
 */
public final class VehiclePlate {

    /** Optional province code, a 2-3 letter series, then 4 digits. */
    private static final Pattern LETTERS =
            Pattern.compile("^(?:([A-Z]{2})[ -]?)?([A-Z]{2,3})[ -]?([0-9]{4})$");
    /** 1-3 digit series without a leading zero, a required separator, then 4 digits. */
    private static final Pattern NUMERIC = Pattern.compile("^([1-9][0-9]{0,2})[ -]([0-9]{4})$");

    private VehiclePlate() {
    }

    /**
     * Checks whether a value is a valid vehicle number plate.
     *
     * @param plate the plate number
     * @return true if the value is a structurally valid plate
     */
    public static boolean isValid(String plate) {
        return tryParse(plate) != null;
    }

    /**
     * Parses a vehicle number plate.
     *
     * @param plate the plate number
     * @return the details of the plate
     * @throws IllegalArgumentException if the value is not a valid plate
     */
    public static VehiclePlateInfo parse(String plate) {
        VehiclePlateInfo info = tryParse(plate);
        if (info == null) {
            throw new IllegalArgumentException("Not a valid Sri Lankan vehicle number plate: " + plate);
        }
        return info;
    }

    private static VehiclePlateInfo tryParse(String plate) {
        String cleaned = Inputs.clean(plate);
        if (cleaned == null) {
            return null;
        }
        String v = cleaned.toUpperCase(Locale.ROOT).replaceAll("\\s+", " ").replaceAll(" ?- ?", "-");

        Matcher m = LETTERS.matcher(v);
        if (m.matches()) {
            Province province = null;
            if (m.group(1) != null) {
                province = Province.fromCode(m.group(1));
                if (province == null) {
                    return null;
                }
            }
            return withNumber(VehiclePlateFormat.LETTERS, province, m.group(2), m.group(3));
        }
        m = NUMERIC.matcher(v);
        if (m.matches()) {
            return withNumber(VehiclePlateFormat.NUMERIC, null, m.group(1), m.group(2));
        }
        return null;
    }

    private static VehiclePlateInfo withNumber(VehiclePlateFormat format, Province province,
                                               String series, String number) {
        if (number.equals("0000")) {
            return null;
        }
        return new VehiclePlateInfo(format, province, series, number);
    }
}
