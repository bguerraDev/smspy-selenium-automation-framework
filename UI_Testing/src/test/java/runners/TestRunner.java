package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"stepDefinitions", "context"},
    plugin = {
            "pretty",
            "html:target/cucumber-reports.html",
            "json:target/cucumber.json"},
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
    
    // This method enables parallel execution of scenarios!
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
