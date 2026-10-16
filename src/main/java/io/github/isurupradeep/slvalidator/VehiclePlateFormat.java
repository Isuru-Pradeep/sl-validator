package io.github.isurupradeep.slvalidator;

/** The vehicle number plate formats used in Sri Lanka. */
public enum VehiclePlateFormat {
    /** Numeric series used before 2000, e.g. {@code 15-1234} or {@code 250-1234}. */
    NUMERIC,
    /** Two or three letter series used since 2000, e.g. {@code KA-1234} or {@code CAB-1234}. */
    LETTERS
}
