# Spring Boot Application

A simple Spring Boot application with Gradle and Java 21.

## Requirements

- Java 21
- Gradle 8.10.2 (or use the included wrapper)

## Running the Application

### Using Gradle Wrapper (Windows)
```bash
gradlew.bat bootRun
```

### Using Gradle Wrapper (Unix/Mac)
```bash
./gradlew bootRun
```

## Testing the Application

Run all tests:
```bash
gradlew.bat test
```

## Endpoints

- **Health Check**: `GET http://localhost:8080/api/health`

## Building the Application

```bash
gradlew.bat build
```

The executable JAR will be created in `build/libs/` directory.

