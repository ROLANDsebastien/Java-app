# Java Application with CI/CD Pipeline

A Spring Boot application built with Gradle, automatically deployed via GitHub Actions to GitHub Container Registry (GHCR).

## Features

- Spring Boot 3.5.9
- Java 17
- Gradle 9.x build system
- CI/CD pipeline with GitHub Actions
- Multi-architecture Docker images (amd64/arm64)
- Automated deployment to GHCR
- Snyk security scanning

## CI/CD Pipeline

The `.github/workflows/ci.yaml` workflow automatically:
- Builds the application with Gradle
- Scans for vulnerabilities using Snyk (dependencies & Docker)
- Uploads Snyk security results to GitHub Security tab (Code Scanning)
- Creates a Docker image with multi-architecture support (linux/amd64, linux/arm64)
- Scans the Docker image for vulnerabilities using Snyk
- Pushes the image to GitHub Container Registry (GHCR)
- Tags images as `latest` (for main branch) and with commit SHA

### Security Scanning

- **Snyk dependency scanning**: Checks Gradle dependencies for vulnerabilities
- **Snyk Docker scanning**: Scans Docker images for security issues
- **GitHub Security tab**: Results are published as SARIF for review
- **Artifacts**: Full reports are available as workflow artifacts

## Running the Application

### Using Docker

1. Pull the latest Docker image from GHCR:
   ```bash
   docker pull ghcr.io/rolandsebastien/java-app:latest
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 ghcr.io/rolandsebastien/java-app:latest
   ```

3. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

## Setup

To enable Snyk security scanning, add your Snyk token as a GitHub secret:
1. Go to your repository Settings > Secrets and variables > Actions
2. Add a new secret named `SNYK_TOKEN` with your Snyk API token

## Development

### Prerequisites

- Java 17
- Gradle 9.x (or use the Gradle wrapper via CI)

### Building Locally

```bash
gradle build
```

The compiled JAR will be available in `build/libs/`.
