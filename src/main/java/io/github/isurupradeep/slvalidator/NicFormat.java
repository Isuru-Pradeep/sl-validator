package io.github.isurupradeep.slvalidator;

/** The two NIC formats issued in Sri Lanka. */
public enum NicFormat {
    /** 9 digits followed by V or X, e.g. {@code 853400937V}. */
    OLD,
    /** 12 digits, issued since 2016, e.g. {@code 198534000937}. */
    NEW
}
