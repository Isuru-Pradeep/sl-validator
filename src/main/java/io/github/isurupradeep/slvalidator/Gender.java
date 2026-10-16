package io.github.isurupradeep.slvalidator;

/** Gender encoded in a Sri Lankan NIC (day-of-year value above 500 means female). */
public enum Gender {
    /** Day-of-year value 1-366. */
    MALE,
    /** Day-of-year value 501-866. */
    FEMALE
}
