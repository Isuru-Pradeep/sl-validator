# Changelog

All notable changes to this project are documented here.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and the project uses
[Semantic Versioning](https://semver.org/).

## [0.1.0] - 2026-10-16

### Added
- `Nic` validation and parsing for old (`853400937V`) and new (`198534000937`) NIC formats.
- Date of birth, gender, age and serial number extraction via `NicInfo`.
- Conversion between old and new NIC formats.
- `Phone` validation for Sri Lankan mobile (070–078, except 073) and landline numbers.
- Phone normalization to E.164 (`+94771234567`) and local (`0771234567`) forms.
- Accepts `+94`, `0094`, `94`, `0` and bare 9-digit phone input with common separators.
- Java 8+ support with no runtime dependencies.

[0.1.0]: https://github.com/Isuru-Pradeep/sl-validator/releases/tag/v0.1.0
