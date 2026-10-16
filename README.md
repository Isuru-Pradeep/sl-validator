# SL Validator 🇱🇰

[![CI](https://github.com/Isuru-Pradeep/sl-validator/actions/workflows/ci.yml/badge.svg)](https://github.com/Isuru-Pradeep/sl-validator/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.isuru-pradeep/sl-validator)](https://central.sonatype.com/artifact/io.github.isuru-pradeep/sl-validator)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

Zero-dependency Java library for validating and parsing **Sri Lankan NIC numbers**, **phone numbers**,
**vehicle number plates** and **postal codes**.

- ✅ Old (`853400937V`) and new (`198534000937`) NIC formats
- ✅ Extract date of birth, gender and age from a NIC, and cross-check it against form fields
- ✅ Convert NICs between old and new formats
- ✅ Validate mobile and landline numbers in any common format, and find a landline's district
- ✅ Normalize phone numbers to E.164 (`+94771234567`), local (`0771234567`) or display form (`077 123 4567`)
- ✅ Parse vehicle number plates (`WP CAB-1234`, `KA-1234`, `15-1234`)
- ✅ Validate postal codes, with province and district reference data
- ✅ Look up provinces, districts and gender from English, Sinhala or Tamil text
- 🛡️ Safe on pasted input: ignores invisible characters and rejects oversized values
- ☕ Java 8+, no runtime dependencies

## Installation

### Maven
```xml
<dependency>
    <groupId>io.github.isuru-pradeep</groupId>
    <artifactId>sl-validator</artifactId>
    <version>0.2.0</version>
</dependency>
```

### Gradle
```groovy
implementation 'io.github.isuru-pradeep:sl-validator:0.2.0'
```

### JitPack

[![JitPack](https://jitpack.io/v/Isuru-Pradeep/sl-validator.svg)](https://jitpack.io/#Isuru-Pradeep/sl-validator)

Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Isuru-Pradeep</groupId>
    <artifactId>sl-validator</artifactId>
    <version>v0.2.0</version>
</dependency>
```

Gradle:
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.Isuru-Pradeep:sl-validator:v0.2.0'
}
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
info.isAdult();                       // true if 18 or older today

Nic.matches("853400937V", LocalDate.of(1985, 12, 5));              // true
Nic.matches("853400937V", LocalDate.of(1985, 12, 5), Gender.MALE); // true

Nic.toNewFormat("853400937V");        // "198534000937"
Nic.toOldFormat("198534000937", 'V'); // "853400937V"
```

`Nic.parse` throws `InvalidNicException` (an `IllegalArgumentException`) with a reason when the input is invalid.

Birth dates after today are rejected. To get results that don't depend on the day the code runs, e.g. in tests, pass the date to treat as today:

```java
Nic.isValid("201016700000", LocalDate.of(2010, 6, 15)); // true
Nic.parse("201016700000", LocalDate.of(2010, 6, 14));   // throws: birth date is in the future
```

### Phone
```java
Phone.isValid("077 123 4567");         // true
Phone.isValidMobile("+94771234567");   // true
Phone.isValidLandline("011-234-5678"); // true
Phone.typeOf("0812234567");            // LANDLINE
Phone.districtOf("0812234567");        // KANDY (null for mobiles)

Phone.toE164("077 123 4567");          // "+94771234567"
Phone.toLocal("0094771234567");        // "0771234567"
Phone.toNational("+94771234567");      // "077 123 4567"
Phone.toInternational("0771234567");   // "+94 77 123 4567"
```

Accepted input forms: `0771234567`, `+94771234567`, `0094771234567`, `94771234567`, `771234567`, with any spaces, dashes, dots or parentheses.

`districtOf` is approximate: area codes belong to exchange towns, and an exchange can serve areas across a district border.

### Vehicle number plate
```java
VehiclePlate.isValid("WP CAB-1234");   // true
VehiclePlate.isValid("15-1234");       // true (old numeric plate)

VehiclePlateInfo plate = VehiclePlate.parse("wpcab1234");
plate.getPlate();                      // "WP CAB-1234"
plate.getProvince();                   // WESTERN (null if the plate has no province)
plate.getSeries();                     // "CAB"
plate.getNumber();                     // "1234"
plate.getFormat();                     // LETTERS
```

The province prefix is optional, since newer plates no longer carry it. Old plates written with the Sinhala "Sri" letter are not supported.

### Postal code
```java
PostalCode.isValid("20000");           // true
PostalCode.isValid("2000");            // false
PostalCode.normalize(" 00700 ");       // "00700"
```

### Provinces and districts
```java
District.KANDY.getProvince();          // CENTRAL
Province.fromCode("NW");               // NORTH_WESTERN

Province.fromName("Western Province"); // WESTERN
Province.fromName("බස්නාහිර පළාත");    // WESTERN
District.fromName("நுவரெலியா");        // NUWARA_ELIYA
District.fromName("nuwara eliya");     // NUWARA_ELIYA

District.KANDY.getSinhalaName();       // "මහනුවර"
District.KANDY.getTamilName();         // "கண்டி"
```

`fromName` ignores case, spacing and a trailing word for "province" or "district" in any of the three languages. Sinhala names match whether or not they were typed with zero-width joiners.

### Gender
```java
Gender.fromText("M");                  // MALE
Gender.fromText("පිරිමි");             // MALE
Gender.fromText("பெண்");               // FEMALE
Gender.fromText("other");              // null
```

## Input rules

- Surrounding whitespace is ignored. Zero-width characters, byte-order marks and soft hyphens, which often come along with copy and paste, are removed, and no-break spaces count as ordinary spaces.
- Inputs longer than 1,024 characters are rejected, never truncated.
- `isValid`, `typeOf`, `districtOf` and the `from...` lookups never throw on bad input. They return `false` or `null`. `parse` and the `to...` conversions throw an `IllegalArgumentException` with a reason.

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
- Validation is structural: it cannot tell whether a number, plate or postal code is actually issued or in service.
- Postal codes are checked for format only, not looked up against the Sri Lanka Post directory.

## Data sources

| Data | Source | Notes |
|------|--------|-------|
| NIC format | No official specification published | Based on widely documented public guidance. The check digit is not verified. |
| Mobile prefixes, area codes | [TRCSL National Numbering Plan](https://www.trc.gov.lk/pages_e.php?id=121) | Area code to district is mapped by exchange town. |
| Postal codes | [Sri Lanka Post](https://slpost.gov.lk/information/postcodes/) | Five-digit format only. |
| Vehicle plates | [Vehicle registration plates of Sri Lanka](https://en.wikipedia.org/wiki/Vehicle_registration_plates_of_Sri_Lanka) | Province codes and series formats. |
| Provinces, districts | [Department of Census and Statistics, Census 2024 preliminary report](https://www.statistics.gov.lk/Resource/en/Population/CPH_2024/Preliminary_Report.pdf) | 9 provinces and 25 districts. Sinhala and Tamil names are widely used spellings; corrections are welcome. |

Found something wrong or outdated? Please open a [data correction issue](https://github.com/Isuru-Pradeep/sl-validator/issues/new?template=data_correction.yml) with a link to the source.

## Contributing

Issues and pull requests are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md). Run the tests with:

```bash
./mvnw verify      # Windows: mvnw.cmd verify
```

No Maven install is needed: the wrapper downloads the right version.

## License

[Apache License 2.0](LICENSE)
