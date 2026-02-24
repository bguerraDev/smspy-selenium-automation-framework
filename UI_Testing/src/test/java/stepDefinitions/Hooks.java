package stepDefinitions;

import org.openqa.selenium.WebDriver;

import context.TestContext;
import context.WarmUpBackend;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.ConfigReader;
import utilities.DriverFactory;
import utilities.ScreenRecorderHelper;

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

        if (ConfigReader.getProperty("screen.recording.enabled").equalsIgnoreCase("true")) {
            try {
                ScreenRecorderHelper.startRecording(scenario.getName().replaceAll(" ", "_"));
            } catch (Exception e) {
                System.out.println("Could not start screen recording: " + e.getMessage());
            }
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) TestContext.getDriver())
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed Screenshot: " + scenario.getName());
            } catch (Exception e) {
                System.out.println("Could not take screenshot: " + e.getMessage());
            }
        }

        if (ConfigReader.getProperty("screen.recording.enabled").equalsIgnoreCase("true")) {
            try {
                ScreenRecorderHelper.stopRecording();
            } catch (Exception e) {
                System.out.println("Could not stop screen recording: " + e.getMessage());
            }
        }

        testContext.removeDriver();
    }
}
