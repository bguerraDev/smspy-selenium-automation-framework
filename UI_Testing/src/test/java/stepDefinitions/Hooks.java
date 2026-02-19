package stepDefinitions;

import context.TestContext;
import context.WarmUpBackend;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;
import utilities.DriverFactory;

public class Hooks {

    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = -30)
    public void warmUpBackendService() {
        WarmUpBackend.wakeUpBackendRender();
    }

    @Before
    public void setUp(Scenario scenario) {

        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("Scenario tags: " + scenario.getSourceTagNames());

        String browser = ConfigReader.getBrowser();
        WebDriver driver = DriverFactory.createDriver(browser); // new factory class
        testContext.initializeDriver(driver);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) testContext.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failed screenshot");
        }
        testContext.removeDriver();
    }
}
