package stepDefinitions;

import api.BaseClient;
import context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;
import utilities.DatabaseHelper;
import utilities.DriverFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

public class Hooks {

    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = -10)
    public void setUp(Scenario scenario) {
        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("Scenario tags: " + scenario.getSourceTagNames());

        String browser = ConfigReader.getBrowser();
        WebDriver driver = DriverFactory.createDriver(browser);
        testContext.initializeDriver(driver);

        boolean hasApiAuthTag = scenario.getSourceTagNames().contains("@api-auth");
        //System.out.println("Has @api-auth tag? " + hasApiAuthTag);

        if (hasApiAuthTag) {
            List<String> usernames = DatabaseHelper.getTestableUsernames();
            testContext.getScenarioContext().setContext("LIVE_TESTABLE_USERNAMES", usernames);
            System.out.println("Loaded " + usernames.size() + " live users for testing");
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) testContext.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failed screenshot");
        }
        testContext.removeDriver();
        testContext.clearScenarioContext();
    }

    @Before(order = 1)
    public void warmUpBackend() {
        System.out.println("Warming up Render backend...");
        BaseClient.executeWithColdStartRetry(() ->
                given().spec(BaseClient.baseSpec).get("/protected/")
        );
        System.out.println("Backend warmed up");

    }
}
