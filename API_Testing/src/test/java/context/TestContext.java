package context;

import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class TestContext {

    // ThreadLocal to ensure each thread (each scenario in parallel) has its own driver
    private final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    // ThreadLocal for ScenarioContext — one per thread/scenario
    private final ThreadLocal<ScenarioContext> scenarioContextThreadLocal = new ThreadLocal<>();

    /**
     * Initializes the driver for the current thread.
     * Called from Hooks @Before
     */
    public void initializeDriver(WebDriver webDriver) {
        if(webDriver == null) {
            throw new IllegalArgumentException("WebDriver cannot be null");
        }
        driverThreadLocal.set(webDriver);
        driverThreadLocal.get().manage().window().maximize();
        driverThreadLocal.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Initialize ScenarioContext for this thread
        scenarioContextThreadLocal.set(new ScenarioContext());
    }

    public ScenarioContext getScenarioContext() {
        ScenarioContext ctx = scenarioContextThreadLocal.get();
        if (ctx == null) {
            throw new IllegalStateException("ScenarioContext not initialized");
        }
        return ctx;
    }

    public WebDriver getDriver() {
        WebDriver webDriver = driverThreadLocal.get();

        if(webDriver == null) {
            throw new IllegalStateException("WebDriver is not initialized for this thread. Call initializeDriver() first.");
        }
        return webDriver;
    }

    /**
     * Removes the driver from the current thread
     * Called from Hooks @After
     */
    public void removeDriver() {
        WebDriver webDriver = driverThreadLocal.get();
        if (webDriver != null) {
            try {
                webDriver.quit(); // close browser
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            driverThreadLocal.remove(); // clear ThreadLocal reference
        }
        // Clear page objects too
    }

    public void clearScenarioContext() {
        ScenarioContext ctx = scenarioContextThreadLocal.get();
        if (ctx != null) {
            ctx.clear();
            scenarioContextThreadLocal.remove();
        }
    }

}
