1.  **How do you ensure your automated tests provide enough information for a developer to debug a failure?**
    *   *Key:* Mention Allure, screenshots on failure, and logging API Request/Response bodies.
2.  **What is the difference between a "Product Defect" and a "Test Defect" in your reporting?**
    *   *Key:* Product = Bug in *smspy*; Test = Flaky locator, network timeout, or setup issue.
3.  **Why do we use an Allure Listener instead of putting screenshot logic inside a `try-catch` in every test?**
    *   *Key:* Separation of concerns. The test stays clean; the listener handles "cross-cutting concerns" like reporting.
4.  **In a headless CI environment, how do you investigate a visual bug?**
    *   *Key:* Using screenshots captured at the exact moment of failure and analyzing the HTML DOM dump.
5.  **What is the benefit of `@Step` annotations for non-technical stakeholders?**
    *   *Key:* It bridges the gap between Gherkin/Business logic and the actual Java execution.
6.  **How do you prevent your reporting from becoming "noisy" with too much data?**
    *   *Key:* Only attach screenshots on failure; use log levels (INFO vs DEBUG).
7.  **How would you integrate Allure with Slack or Microsoft Teams?**
    *   *Key:* Using a CI/CD plugin or a webhook in GitHub Actions to send a summary and a link to the report.
8.  **If a test passes but takes 30 seconds instead of 2, how does your reporting help?**
    *   *Key:* Allure tracks execution time per step, helping identify which specific action is slow.
9.  **Describe a time you used automation logs to find a bug that was not visible on the UI.**
    *   *Key:* Talk about an API response returning a `500 Error` but the UI failing silently or showing a generic message.
10. **Why is it important to store "History" in your Allure reports in a CI pipeline?**
    *   *Key:* To identify "Flaky" tests that fail intermittently (Trends).