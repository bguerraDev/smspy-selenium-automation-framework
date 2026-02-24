#### 1. What is Observability?
In QA, observability is the ability to understand the internal state of a test execution by looking at the data it produces.
*   **The Artifacts:** Screenshots (UI), JSON Payloads (API), Console Logs (Application), and Video (User Journey).

#### 2. Allure Report Strategy
*   **Categories:** Allure allows you to categorize defects into "Product Defects" (Real bugs) and "Test Defects" (Flaky tests/Environment issues). This is crucial for Dublin startups to track framework health.
*   **The Power of @Step:** By annotating methods with `@Step("User logs in with username {0}")`, the technical code is translated into a business story in the report. This is what you show to Product Managers.

#### 3. API Logging with RestAssured
Using the `AllureRestAssured` filter is a "Pro" move. It captures:
*   **Curl commands:** Developers love this because they can copy the failed request from your report and run it in their terminal immediately.
*   **Status Codes & Latency:** Helps identify performance bottlenecks in the *smspy* backend.

#### 4. Handling Video in CI vs. Local
*   **Local:** Great for debugging complex UI flows on your machine.
*   **CI (GitHub Actions):** Video is often disabled or replaced by "Traces" (like in Playwright) or high-frequency screenshots because video rendering slows down the pipeline and increases costs.