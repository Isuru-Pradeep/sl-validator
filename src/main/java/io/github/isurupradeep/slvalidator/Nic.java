package io.github.isurupradeep.slvalidator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Validation, parsing and conversion of Sri Lankan National Identity Card (NIC) numbers.
 *
 * <p>Old format: {@code YYDDDSSSC} + {@code V}/{@code X}. New format: {@code YYYYDDDSSSSC}.
 * {@code DDD} is the day of the year (1-366), plus 500 for females. The NIC day count always
 * treats February as having 29 days, so in a non-leap year day 60 does not exist.</p>
 *
 * <p>The check digit is not verified because its algorithm is not officially published.</p>
 */
public final class Nic {

    private static final Pattern OLD = Pattern.compile("^[0-9]{9}[VX]$");
    private static final Pattern NEW = Pattern.compile("^[0-9]{12}$");
    private static final int FEMALE_OFFSET = 500;
    private static final int MIN_YEAR = 1900;
    private static final int[] DAYS_IN_MONTH = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private Nic() {
    }

    /**
     * Checks whether a value is a valid NIC number.
     *
     * @param nic the NIC number, old or new format
     * @return true if the value is a structurally valid NIC with a real birth date not in the future
     */
    public static boolean isValid(String nic) {
        return isValid(nic, LocalDate.now());
    }

    /**
     * Checks whether a value is a valid NIC number, treating {@code today} as the current date.
     * Use this for results that do not change with the day the code runs, e.g. in tests.
     *
     * @param nic the NIC number, old or new format
     * @param today the date after which a birth date counts as being in the future
     * @return true if the value is a structurally valid NIC with a birth date not after {@code today}
     */
    public static boolean isValid(String nic, LocalDate today) {
        try {
            parse(nic, today);
            return true;
        } catch (InvalidNicException e) {
            return false;
        }
    }

    /**
     * Parses a NIC number, accepting surrounding whitespace, lower-case {@code v}/{@code x} and
     * invisible characters picked up by copy and paste.
     *
     * @param nic the NIC number, old or new format
     * @return the details encoded in the NIC
     * @throws InvalidNicException if the value is not a valid NIC
     */
    public static NicInfo parse(String nic) {
        return parse(nic, LocalDate.now());
    }

    /**
     * Parses a NIC number, treating {@code today} as the current date.
     *
     * @param nic the NIC number, old or new format
     * @param today the date after which a birth date counts as being in the future
     * @return the details encoded in the NIC
     * @throws InvalidNicException if the value is not a valid NIC
     */
    public static NicInfo parse(String nic, LocalDate today) {
        Objects.requireNonNull(today, "today");
        String value = normalize(nic);
        int year;
        int day;
        String serial;
        NicFormat format;

        if (OLD.matcher(value).matches()) {
            format = NicFormat.OLD;
            year = 1900 + Integer.parseInt(value.substring(0, 2));
            day = Integer.parseInt(value.substring(2, 5));
            serial = value.substring(5, 8);
        } else if (NEW.matcher(value).matches()) {
            format = NicFormat.NEW;
            year = Integer.parseInt(value.substring(0, 4));
            if (year < MIN_YEAR) {
                throw new InvalidNicException("Birth year " + year + " is before " + MIN_YEAR);
            }
            day = Integer.parseInt(value.substring(4, 7));
            serial = value.substring(7, 11);
        } else {
            throw new InvalidNicException("NIC must be 9 digits followed by V/X, or 12 digits");
        }

        Gender gender = Gender.MALE;
        if (day > FEMALE_OFFSET) {
            gender = Gender.FEMALE;
            day -= FEMALE_OFFSET;
        }

        LocalDate dob = toDate(year, day);
        if (dob.isAfter(today)) {
            throw new InvalidNicException("Birth date " + dob + " is in the future");
        }
        return new NicInfo(value, format, dob, gender, serial);
    }

    /**
     * Checks whether a NIC is valid and encodes the given date of birth, e.g. to cross-check
     * a form where the NIC and birth date are entered separately.
     *
     * @param nic the NIC number, old or new format
     * @param dateOfBirth the expected date of birth
     * @return true if the NIC is valid and its date of birth equals {@code dateOfBirth}
     */
    public static boolean matches(String nic, LocalDate dateOfBirth) {
        return isValid(nic) && parse(nic).getDateOfBirth().equals(dateOfBirth);
    }

    /**
     * Checks whether a NIC is valid and encodes the given date of birth and gender.
     *
     * @param nic the NIC number, old or new format
     * @param dateOfBirth the expected date of birth
     * @param gender the expected gender
     * @return true if the NIC is valid and both values match
     */
    public static boolean matches(String nic, LocalDate dateOfBirth, Gender gender) {
        return matches(nic, dateOfBirth) && parse(nic).getGender() == gender;
    }

    /**
     * Converts an old NIC to the new 12-digit format, e.g. {@code 853400937V -> 198534000937}.
     * A new NIC is returned unchanged.
     *
     * @param nic the NIC number, old or new format
     * @return the NIC in the new 12-digit format
     * @throws InvalidNicException if the value is not a valid NIC
     */
    public static String toNewFormat(String nic) {
        NicInfo info = parse(nic);
        if (info.getFormat() == NicFormat.NEW) {
            return info.getNic();
        }
        String v = info.getNic();
        return "19" + v.substring(0, 5) + "0" + v.substring(5, 9);
    }

    /**
     * Converts a new NIC to the old format, e.g. {@code 198534000937 -> 853400937V}.
     * Only possible for birth years 1900-1999 whose serial starts with 0.
     * An old NIC is returned unchanged, keeping its own suffix.
     *
     * @param nic the NIC number, old or new format
     * @param suffix {@code 'V'} or {@code 'X'}
     * @return the NIC in the old format
     * @throws IllegalArgumentException if the suffix is not V or X
     * @throws InvalidNicException if the NIC cannot be represented in the old format
     */
    public static String toOldFormat(String nic, char suffix) {
        char s = Character.toUpperCase(suffix);
        if (s != 'V' && s != 'X') {
            throw new IllegalArgumentException("Suffix must be V or X");
        }
        NicInfo info = parse(nic);
        if (info.getFormat() == NicFormat.OLD) {
            return info.getNic();
        }
        String v = info.getNic();
        if (!v.startsWith("19") || v.charAt(7) != '0') {
            throw new InvalidNicException("NIC " + v + " has no old-format equivalent");
        }
        return v.substring(2, 7) + v.substring(8, 12) + s;
    }

    private static String normalize(String nic) {
        if (nic == null) {
            throw new InvalidNicException("NIC must not be null");
        }
        String value = Inputs.clean(nic);
        if (value == null) {
            throw new InvalidNicException("NIC is longer than " + Inputs.MAX_LENGTH + " characters");
        }
        return value.replace(" ", "").toUpperCase(Locale.ROOT);
    }

    private static LocalDate toDate(int year, int dayOfYear) {
        if (dayOfYear < 1 || dayOfYear > 366) {
            throw new InvalidNicException("Day-of-year value " + dayOfYear + " is out of range");
        }
        int month = 0;
        int remaining = dayOfYear;
        while (remaining > DAYS_IN_MONTH[month]) {
            remaining -= DAYS_IN_MONTH[month];
            month++;
        }
        try {
            return LocalDate.of(year, month + 1, remaining);
        } catch (DateTimeException e) {
            throw new InvalidNicException("Invalid birth date: Feb 29 in non-leap year " + year);
        }
    }
}
