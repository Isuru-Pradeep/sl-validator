package io.github.isurupradeep.slvalidator;

import java.time.LocalDate;
import java.time.Period;

/** Immutable result of parsing a valid NIC number. */
public final class NicInfo {
    private final String nic;
    private final NicFormat format;
    private final LocalDate dateOfBirth;
    private final Gender gender;
    private final String serialNumber;

    NicInfo(String nic, NicFormat format, LocalDate dateOfBirth, Gender gender, String serialNumber) {
        this.nic = nic;
        this.format = format;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.serialNumber = serialNumber;
    }

    /**
     * The NIC in canonical form.
     *
     * @return the NIC, upper-case with no spaces
     */
    public String getNic() { return nic; }

    /**
     * The format of the NIC.
     *
     * @return whether the NIC is in the old or new format
     */
    public NicFormat getFormat() { return format; }

    /**
     * The date of birth encoded in the NIC.
     *
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() { return dateOfBirth; }

    /**
     * The gender encoded in the NIC.
     *
     * @return the gender
     */
    public Gender getGender() { return gender; }

    /**
     * Serial digits after the birth date, excluding the check digit.
     *
     * @return 3 digits for old NICs, 4 for new
     */
    public String getSerialNumber() { return serialNumber; }

    /**
     * Age in completed years on the given date.
     *
     * @param onDate the date to calculate the age on
     * @return the age in completed years
     */
    public int getAge(LocalDate onDate) {
        return Period.between(dateOfBirth, onDate).getYears();
    }

    /**
     * Age in completed years today.
     *
     * @return the age in completed years
     */
    public int getAge() {
        return getAge(LocalDate.now());
    }

    @Override
    public String toString() {
        return "NicInfo{nic=" + nic + ", format=" + format + ", dateOfBirth=" + dateOfBirth
                + ", gender=" + gender + "}";
    }
}
