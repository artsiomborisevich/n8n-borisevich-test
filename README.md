# Spring Boot Application

A simple Spring Boot application with Gradle and Java 21.

## Requirements

- Java 21
- Gradle 8.10.2 (or use the included wrapper)

Run all tests:
```bash
gradlew.bat test
```

## Running the Application

### Using Gradle Wrapper (Windows)
```bash
gradlew.bat bootRun
```

## Endpoints

- **Health Check**: `GET http://localhost:8080/api/health`

## Building the Application

```bash
gradlew.bat build
```

The executable JAR will be created in `build/libs/` directory.

