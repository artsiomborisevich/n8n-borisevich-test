# GitHub Actions CI/CD Pipeline

## Overview
This directory contains GitHub Actions workflows for the n8n-borisevich-test project. The main pipeline automates building, testing, security scanning, and Docker image publishing.

## Workflows

### ci-cd-pipeline.yml
Main CI/CD pipeline that handles the complete build, test, scan, and deploy cycle.

#### Trigger Events
- **Push**: Runs on pushes to `main`, `dev/*`, and `feature/*` branches
- **Pull Request**: Runs on PRs targeting `main` branch

#### Pipeline Jobs

##### 1. Build and Test
**Purpose**: Compile the Java application and run unit tests

**Steps**:
- Checkout code
- Set up JDK 21 (Temurin distribution)
- Grant execute permission for gradlew
- Build with Gradle (excluding tests initially)
- Run all tests
- Upload test results and build artifacts

**Artifacts**:
- `test-results`: JUnit test results
- `build-artifacts`: Compiled JAR files

##### 2. Security Scan
**Purpose**: Scan dependencies for known vulnerabilities and auto-fix when possible

**Steps**:
- OWASP Dependency Check scan (fails on CVSS >= 7)
- Gradle dependency vulnerability analysis
- Auto-fix vulnerable dependencies using dependency updates plugin
- Create PR with security fixes if applicable

**Security Thresholds**:
- `CRITICAL`: Must be fixed before merge/deploy
- `HIGH`: Should be fixed in next release
- `MEDIUM`: Advisory, can be fixed later
- Fails build on CVSS score >= 7

**Auto-Fix Behavior**:
- If fixable vulnerabilities are found, creates a PR with dependency updates
- PR title: "Security: Auto-fix vulnerable dependencies"
- Branch: `security/auto-fix-dependencies`

**Artifacts**:
- `dependency-check-report`: HTML, JSON, and SARIF reports

##### 3. Build and Scan Docker Image
**Purpose**: Build Docker image, scan for vulnerabilities, and push to GitHub Container Registry

**Steps**:
- Build Docker image using BuildKit
- Run Trivy vulnerability scanner (CRITICAL, HIGH, MEDIUM severities)
- Upload scan results to GitHub Security tab
- Fail on CRITICAL unfixed vulnerabilities
- Push image to GitHub Container Registry (ghcr.io)
- Generate deployment summary

**Image Tags**:
- `<branch>`: Current branch name
- `<branch>-<sha>`: Branch + commit SHA
- `pr-<number>`: PR number (for pull requests)
- `latest`: Latest from default branch

**Registry**: `ghcr.io/<github-username>/n8n-borisevich-test`

**Security Scan**:
- Uses Aqua Security Trivy scanner
- Scans for OS and application vulnerabilities
- Results uploaded to GitHub Security Dashboard
- Fails build if CRITICAL vulnerabilities found

## Configuration

### Required Secrets
- `GITHUB_TOKEN`: Automatically provided by GitHub Actions (for GHCR push)

### Gradle Plugins Added
1. **OWASP Dependency Check** (`org.owasp.dependencycheck:9.0.9`)
   - Scans dependencies for known CVEs
   - Configuration in `build.gradle:40-45`

2. **Dependency Updates** (`com.github.ben-manes.versions:0.51.0`)
   - Checks for dependency updates
   - Configuration in `build.gradle:48-58`

### Suppression File
`dependency-check-suppressions.xml` - Add CVE suppressions here for false positives or accepted risks

## Usage Examples

### Running Locally

#### Build and Test
```bash
./gradlew clean build test
```

#### Security Scan
```bash
./gradlew dependencyCheckAnalyze
```

#### Check for Dependency Updates
```bash
./gradlew dependencyUpdates
```

#### Build Docker Image
```bash
docker build -t n8n-borisevich-test:local .
```

#### Scan Docker Image with Trivy
```bash
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock \
  aquasec/trivy:latest image n8n-borisevich-test:local
```

### Viewing Pipeline Results

#### Test Results
- Navigate to: Actions → Workflow Run → Artifacts → `test-results`

#### Security Scan Results
- Navigate to: Security → Code scanning alerts
- Or: Actions → Workflow Run → Artifacts → `dependency-check-report`

#### Docker Image
- Navigate to: Packages → n8n-borisevich-test
- Or: `ghcr.io/<github-username>/n8n-borisevich-test`

### Pull Docker Image
```bash
docker pull ghcr.io/<github-username>/n8n-borisevich-test:latest
```

### Run Docker Container
```bash
docker run -d -p 8080:8080 \
  --name n8n-test \
  ghcr.io/<github-username>/n8n-borisevich-test:latest
```

## Pipeline Flow

```
┌─────────────────────┐
│  Push / PR Trigger  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Build and Test     │
│  - Compile Java 21  │
│  - Run unit tests   │
│  - Upload artifacts │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Security Scan      │
│  - OWASP Dep Check  │
│  - Auto-fix vulns   │
│  - Create Fix PR    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│  Build & Scan Image │
│  - Build Docker     │
│  - Trivy scan       │
│  - Upload to GHCR   │
│  - Generate summary │
└─────────────────────┘
```

## Troubleshooting

### Build Failures
- Check Java version (must be 21)
- Verify Gradle wrapper permissions
- Review test results artifact

### Security Scan Failures
- Review dependency-check-report artifact
- Check if CVSS >= 7 vulnerabilities exist
- Add suppressions to `dependency-check-suppressions.xml` if false positive

### Docker Image Scan Failures
- Review Trivy scan results in Security tab
- Check for CRITICAL vulnerabilities
- Update base image in Dockerfile if needed

### Image Push Failures
- Verify GITHUB_TOKEN permissions
- Check if package already exists (may need manual deletion)
- Ensure GHCR is enabled for repository

## Best Practices

1. **Never commit secrets** - Use GitHub Secrets for sensitive data
2. **Review security PRs** - Always review auto-generated security fix PRs before merging
3. **Keep dependencies updated** - Run `dependencyUpdates` task regularly
4. **Monitor security alerts** - Check GitHub Security tab regularly
5. **Resource limits** - Ensure Docker containers have proper resource constraints in production

## References
- [OWASP Dependency Check](https://jeremylong.github.io/DependencyCheck/)
- [Trivy Scanner](https://github.com/aquasecurity/trivy)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [GitHub Actions](https://docs.github.com/en/actions)
