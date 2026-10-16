# Changelog

All notable changes to this project are documented here.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project uses
[Semantic Versioning](https://semver.org/).

## [0.3.0] - Unreleased

### Added
- `Nic.parse(nic, today)` and `Nic.isValid(nic, today)` to fix the date used for the future-birth-date check.
- `Phone.toNational` (`077 123 4567`) and `Phone.toInternational` (`+94 77 123 4567`) display formats.
- Sinhala and Tamil names on `Province` and `District`, and `Province.fromName` / `District.fromName`
  lookups that accept English, Sinhala or Tamil.
- `Gender.fromText` to normalize free-text gender input in English, Sinhala or Tamil.
- Data sources section in the README, contributing guide, security policy and issue templates.

### Changed
- All validators ignore zero-width characters, byte-order marks and soft hyphens, and treat no-break
  spaces as ordinary spaces. Pasted values that used to be rejected are now accepted.
- Inputs longer than 1,024 characters are rejected.

## [0.2.0] - 2026-10-16

### Added
- `VehiclePlate` validation and parsing for letter-series plates (`CAB-1234`, `KA-1234`) with an
  optional province prefix (`WP CAB-1234`), and old numeric plates (`15-1234`).
- `PostalCode` validation for five-digit Sri Lankan postal codes.
- `Province` and `District` enums covering all 9 provinces and 25 districts.
- `Phone.districtOf` to find the district of a landline's area code.
- `Nic.matches` to cross-check a NIC against a date of birth and gender.
- `NicInfo.isAdult` to check whether the holder is 18 or older.

## [0.1.1] - 2026-10-16

### Changed
- First release published to Maven Central (`io.github.isuru-pradeep:sl-validator`).
  No code changes from 0.1.0, which was released through JitPack only.

## [0.1.0] - 2026-10-16

### Added
- `Nic` validation and parsing for old (`853400937V`) and new (`198534000937`) NIC formats.
- Date of birth, gender, age and serial number extraction via `NicInfo`.
- Conversion between old and new NIC formats.
- `Phone` validation for Sri Lankan mobile (070–078, except 073) and landline numbers.
- Phone normalization to E.164 (`+94771234567`) and local (`0771234567`) forms.
- Accepts `+94`, `0094`, `94`, `0` and bare 9-digit phone input with common separators.
- Java 8+ support with no runtime dependencies.

[0.2.0]: https://github.com/Isuru-Pradeep/sl-validator/releases/tag/v0.2.0
[0.1.1]: https://github.com/Isuru-Pradeep/sl-validator/releases/tag/v0.1.1
[0.1.0]: https://github.com/Isuru-Pradeep/sl-validator/releases/tag/v0.1.0
