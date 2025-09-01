# Tutorial how to build source code on github

## 1️⃣ What is CI/CD?
**CI (Continuous Integration)**:
- Automatically builds and tests your code whenever you push changes.
- Detects bugs early.
- Ensures all code merges cleanly into the main branch.
**CD (Continuous Delivery / Continuous Deployment)**:
- Continuous Delivery: Automatically prepares the application for release; deployment is manual.
- Continuous Deployment: Automatically deploys to production after passing all tests.
- CI/CD together allows developers to release software faster and more reliably.

## 2️⃣ CI/CD Flow for a Spring Boot Project
1. Developer pushes code to GitHub.
2. CI pipeline is triggered:
- Checkout code
- Set up Java environment
- Build project (mvn clean install)
- Run unit tests
- Run integration tests (e.g., with MySQL via Testcontainers)
3. Artifacts (JAR/WAR files) are uploaded to GitHub Actions or artifact repository.
4. CD pipeline (optional):
- Deploy to staging or production environment
- Can use Docker, Kubernetes, AWS, Azure, etc.

## 3️⃣ GitHub Actions CI/CD Example
Here’s a simple CI/CD workflow for a Spring Boot project:
```markdown
name: Java CI with Maven (JDK 19)

# Trigger on push or pull request to main branch
on:
  push:
    branches: [ master ]
  pull_request:
    branches: [ master ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      # Step 1: Checkout code
      - name: Checkout repository
        uses: actions/checkout@v3

      # Step 2: Set up JDK 19
      - name: Set up JDK 19
        uses: actions/setup-java@v3
        with:
          java-version: '19'
          distribution: 'temurin'

      # Step 3: Cache Maven dependencies
      - name: Cache Maven packages
        uses: actions/cache@v3
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      # Step 4: Build project
      - name: Build with Maven
        run: mvn clean install --batch-mode -DskipTests

      # Step 5: Run tests
      - name: Run tests
        run: mvn test --batch-mode -DskipTests

      # Step 6: Upload JAR artifact
      - name: Upload task-service JAR
        uses: actions/upload-artifact@v4
        with:
          name: task-service
          path: task-service/target/*.jar
      - name: Upload user-service JAR
        uses: actions/upload-artifact@v4
        with:
          name: user-service
          path: user-service/target/*.jar
      - name: Upload api-gateway-service JAR
        uses: actions/upload-artifact@v4
        with:
          name: api-gateway-service
          path: api-gateway-service/target/*.jar
```
