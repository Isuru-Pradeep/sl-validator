# SL Validator 🇱🇰

[![CI](https://github.com/Isuru-Pradeep/sl-validator/actions/workflows/ci.yml/badge.svg)](https://github.com/Isuru-Pradeep/sl-validator/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.isuru-pradeep/sl-validator)](https://central.sonatype.com/artifact/io.github.isuru-pradeep/sl-validator)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

Zero-dependency Java library for validating and parsing **Sri Lankan NIC numbers** and **phone numbers**.

- ✅ Old (`853400937V`) and new (`198534000937`) NIC formats
- ✅ Extract date of birth, gender and age from a NIC
- ✅ Convert NICs between old and new formats
- ✅ Validate mobile and landline numbers in any common format
- ✅ Normalize phone numbers to E.164 (`+94771234567`) or local (`0771234567`)
- ☕ Java 8+, no runtime dependencies

## Installation

### Maven
```xml
<dependency>
    <groupId>io.github.isuru-pradeep</groupId>
    <artifactId>sl-validator</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.github.isuru-pradeep:sl-validator:0.1.0'
```

## Usage

### NIC
```java
Nic.isValid("853400937V");            // true
Nic.isValid("850600000V");            // false (Feb 29 1985 doesn't exist)

NicInfo info = Nic.parse("853400937V");
info.getDateOfBirth();                // 1985-12-05
info.getGender();                     // MALE
info.getFormat();                     // OLD
info.getAge();                        // age today

Nic.toNewFormat("853400937V");        // "198534000937"
Nic.toOldFormat("198534000937", 'V'); // "853400937V"
```

`Nic.parse` throws `InvalidNicException` (an `IllegalArgumentException`) with a reason when the input is invalid.

### Phone
```java
Phone.isValid("077 123 4567");         // true
Phone.isValidMobile("+94771234567");   // true
Phone.isValidLandline("011-234-5678"); // true
Phone.typeOf("0812234567");            // LANDLINE

Phone.toE164("077 123 4567");          // "+94771234567"
Phone.toLocal("0094771234567");        // "0771234567"
```

Accepted input forms: `0771234567`, `+94771234567`, `0094771234567`, `94771234567`, `771234567`, with any spaces, dashes, dots or parentheses.

## How NIC numbers work

| Format | Layout | Example |
|--------|--------|---------|
| Old | `YY` `DDD` `SSS` `C` + `V`/`X` | `85` `340` `093` `7` `V` |
| New | `YYYY` `DDD` `SSSS` `C` | `1985` `340` `0093` `7` |

- `DDD` is the day of the year, **+500 for females**.
- The day count always assumes February has 29 days, so in non-leap years day 60 is invalid.
- An old NIC converts to new by prefixing `19` to the year and `0` to the serial.
- The check digit `C` is not verified because its algorithm is not officially published.

## Limitations

- Mobile operator is not reported, since number portability makes prefix-based detection unreliable.
- Validation is structural: it cannot tell whether a number is actually issued or in service.

## Contributing

Issues and pull requests are welcome. Run the tests with:

```bash
./mvnw verify      # Windows: mvnw.cmd verify
```

No Maven install is needed: the wrapper downloads the right version.

## License

[Apache License 2.0](LICENSE)
