# Production-Grade QA Automation Framework – SMSPy Messaging App

[![SMSPY Automation CI](https://github.com/bguerraDev/smspy-selenium-automation-framework/actions/workflows/main.yml/badge.svg)](https://github.com/bguerraDev/smspy-selenium-automation-framework/actions/workflows/main.yml)

![Java](https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=java&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.27.0-green?style=for-the-badge&logo=selenium&logoColor=white)
![Cucumber](https://img.shields.io/badge/Cucumber-7.20.1-brightgreen?style=for-the-badge&logo=cucumber&logoColor=white)
![Rest Assured](https://img.shields.io/badge/Rest%20Assured-6.0.0-orange?style=for-the-badge&logo=rest-assured&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red?style=for-the-badge&logo=testng&logoColor=white)
![Allure Reports](https://img.shields.io/badge/Allure-2.29.0-purple?style=for-the-badge&logo=allure-report&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.9-c17a00?style=for-the-badge&logo=apache-maven&logoColor=white)

**Modern Java test automation framework** covering **API-first**, **UI E2E**, **security validation** and **observability** for a real-world **React + Django** messaging platform hosted on Render.

**Live demo:**
>Frontend → https://smspy-frontend-pre.onrender.com/

>Backend API → https://smspy-backend-pre.onrender.com/api/protected/

## Why This Framework Matters for Recruiters
- Demonstrates **industry-standard QA practices** used at Stripe, Revolut, Google, UBS, Avaloq…
- **80%+ business logic coverage** via fast & reliable API tests
- **Security testing mindset** — OWASP API Top 10 coverage (BOLA/IDOR, SQLi, auth bypass…)
- **Production hardening** — cold-start warm-up, flakiness mitigation, parallel-safe execution
- **Beautiful & informative reporting** — Allure with request payloads, screenshots, timelines

## Technologies Used

| Layer       | Tools & Libraries                          | Purpose                              |
|-------------|--------------------------------------------|--------------------------------------|
| API         | Rest Assured 6.0.0, Cucumber 7.20.1, TestNG 7.10.2 | API contract & security testing     |
| UI          | Selenium 4.27.0, Cucumber 7.20.1, TestNG 7.10.2 | End-to-end browser automation       |
| Reporting   | Allure 2.29.0 (TestNG + Cucumber adapters) | Beautiful reports + screenshots     |
| Stability   | Awaitility 4.3.0                           | Polling for cold starts & flakiness |
| Build       | Maven 3.9.x                                | Multi-module project management     |
| Backend     | Render-hosted Django + Neon PostgreSQL     | Real-world cloud environment        |
| Frontend    | React + Tailwind (Render-hosted)           | Modern SPA testing                  |

## Key Features & Highlights

### API Layer (API_Testing)

- **Full business flows**: Authentication (JWT), messaging (text + image multipart), profile avatar updates
- **Security testing suite**: SQL injection, authentication bypass, BOLA/IDOR, error leak prevention
- **Database integration**: Real Neon PostgreSQL validation of user/message data
- **Cold-start resilience**: Awaitility-based backend warm-up for Render-hosted API
- **Observability**: Allure + RestAssured filter (full request/response logging)

### UI Layer (UI_Testing)

- **Page Object Model** with lazy initialization & thread-safety
- **End-to-end user journeys**: Login → send text/image message → verify inbox → profile avatar update
- **Advanced handling**:
    - Toast notifications (fade-out detection + optional close button click)
    - File upload with hidden `<input type="file">` + styled label trigger
    - Dynamic dropdown selection with Awaitility polling
- **Stability**:
    - Backend warm-up before UI scenarios
    - Awaitility for flakiness (dropdown population, toast disappearance)
- **Reporting**: Allure screenshots + logs on failed/broken tests

### Cross-Cutting Concerns

- **Thread-safety & parallel execution**: ThreadLocal WebDriver + PicoContainer context
- **Configuration**: Unified `config.properties` (URLs, browser, toggles)
- **Observability**: Allure reports with screenshots, logs, steps, timeline
- **CI/CD readiness**: Modular Maven structure (parent + sub-modules)
- **Real DB assertions**

## Project Structure

```
smspy-selenium-automation-framework/
├── API_Testing/                # API automation layer (Rest Assured + Cucumber + TestNG)
│   ├── src/test/java/api/      # API clients, base spec
│   ├── src/test/java/context/  # Shared context, thread-safe state
│   ├── src/test/java/stepDefinitions/
│   ├── src/test/resources/features/
│   └── pom.xml
├── UI_Testing/                 # UI automation layer (Selenium + POM + Cucumber + TestNG)
│   ├── src/test/java/pages/    # Page Object Model
│   ├── src/test/java/listeners/# Allure listener (screenshots on failure/broken)
│   ├── src/test/java/stepDefinitions/
│   ├── src/test/resources/features/
│   └── pom.xml
├── .gitignore
└── README.md
```

## Setup & Run

```bash
# API tests
cd API_Testing
mvn clean test -DsuiteXmlFile=api-testng.xml

# UI tests (Chrome default)
cd UI_Testing
mvn clean test -DsuiteXmlFile=ui-testng.xml

# Allure report
allure serve target/allure-results