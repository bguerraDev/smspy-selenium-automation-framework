package stepDefinitions;

import context.ScenarioContext;
import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.LoginPage;
import utilities.ConfigReader;

public class UiAuthSteps {

    private final TestContext testContext;
    private final ScenarioContext scenarioContext;
    private LoginPage loginPage;

    public UiAuthSteps(TestContext testContext, ScenarioContext scenarioContext) {
        this.testContext = testContext;
        this.scenarioContext = scenarioContext;
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        testContext.getLoginPage().open();
    }

    @When("I login as {string} with password")
    public void iLoginAsWithPassword(String username) {
        loginPage = testContext.getLoginPage();
        String password = ConfigReader.getProperty("bryan1.password"); // TODO only for this scenario . Is not a good practice
        loginPage.login(username, password);
    }
}