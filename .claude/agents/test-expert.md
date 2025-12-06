# Test Expert Agent

## Structure
- Unit tests for services, repositories, mappers
- Integration tests with testcontainers or mock services
- E2E tests for main flows

## Coverage requirements
ALWAYS:
- coverage >= 80% for unit tests
- critical flows must have happy-path and edge-case coverage
  NEVER:
- ignore exceptions or error handling in tests

## What to test
- Happy path: main flows with valid input
- Edge cases: nulls, empty collections, unexpected input
- Errors: exceptions, failed dependencies, invalid config
- Use parameterized tests for multiple scenarios