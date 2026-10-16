# Contributing to sl-validator

Thanks for helping. Bug reports, data corrections and pull requests are all welcome.

## Building

No Maven install is needed:

```bash
./mvnw verify      # Windows: mvnw.cmd verify
```

This compiles for Java 8, runs all tests and builds the javadoc. CI runs the same command on Java 8, 11, 17 and 21.

## Ground rules

- **Zero runtime dependencies.** The library must stay dependency-free; test dependencies are fine.
- **Java 8 compatible.** Do not use APIs newer than Java 8 (`List.of`, `var`, `String.repeat`, ...).
- **Verifiable rules only.** A new validator or reference table needs a link to an official or
  authoritative source, cited in the pull request and in the README's data sources section.
- **No real personal data.** Use made-up NIC and phone numbers in tests, issues and examples.
- **Tests with every change.** Cover both accepted and rejected inputs, including edge cases.
- **Public API needs javadoc.** The build fails on javadoc errors.

## Sinhala and Tamil text

Names in Sinhala and Tamil should be checked by a native speaker. If you spot a wrong or
unusual spelling, please open a *Data correction* issue.

## Pull requests

Open pull requests against `main`. CI must pass before merging. Keep each pull request focused on one change.
