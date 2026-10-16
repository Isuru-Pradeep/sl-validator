package io.github.isurupradeep.slvalidator;

/** Thrown when a string is not a valid Sri Lankan NIC number. */
public class InvalidNicException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param message the reason the NIC is invalid
     */
    public InvalidNicException(String message) {
        super(message);
    }
}
