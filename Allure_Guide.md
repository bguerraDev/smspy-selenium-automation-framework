# Allure Reporting & Observability Guide

This guide explains how to generate, open, and interpret Allure reports for the `smspy-selenium-automation-framework`.

## 1. Local Report Generation

To generate and open the report locally after running your tests, follow these steps:

### Prerequisites
- Install Allure Commandline: `npm install -g allure-commandline` or `brew install allure`.
- Ensure `allure-results` folder exists (generated automatically by TestNG/Cucumber).

### Commands
Run the following from the root or module directory:

```bash
# Generate and open a temporary local server
allure serve allure-results
```

Alternatively, to generate a static HTML report:
```bash
allure generate allure-results --clean -o allure-report
allure open allure-report
```

## 2. Evidence & Screen Recording

### Screenshot on Failure
The `AllureListener` automatically captures a screenshot when a UI test fails and attaches it to the report under the "Attachments" section.

### API Logs
All API Request/Response details (Headers, Body) are automatically logged thanks to the `AllureRestAssured` filter in `BaseClient`.

### Screen Recording (Monte Screen Recorder)
For local evidence beyond screenshots, we suggest using **Monte Screen Recorder**. 

**Lightweight Implementation Suggestion:**
1. Add the dependency to `pom.xml`:
   ```xml
   <dependency>
       <groupId>com.github.stephenc.monte</groupId>
       <artifactId>monte-screen-recorder</artifactId>
       <version>0.7.7.0</version>
   </dependency>
   ```
2. Toggle recording in `config.properties`:
   `screen.recording.enabled=true`
3. Use a utility class to start/stop recording in `@Before` and `@After` hooks.

## 3. Business Readability (@Step)
We use Allure `@Step` annotations in Page Objects and API Clients to make technical code segments human-readable in the report.
- **UI:** Each click, type, and navigation appears as a clear step.
- **API:** Login and messaging actions recorded as logical blocks.

---
*Happy Testing with smspy!*
