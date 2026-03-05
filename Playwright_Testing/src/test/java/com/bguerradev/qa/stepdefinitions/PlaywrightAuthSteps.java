package com.bguerradev.qa.stepdefinitions;

import com.bguerradev.qa.context.TestContext;
import com.bguerradev.qa.pages.LoginPage;
import com.bguerradev.qa.utilities.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class PlaywrightAuthSteps {
    private final TestContext testContext;

    public PlaywrightAuthSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        testContext.getLoginPage().open(ConfigReader.getProperty("base.url.login", "https://smspy-frontend-pre.onrender.com/"));
    }

    @When("I login as {string}")
    public void iLoginAs(String username) {
        String password = ConfigReader.getProperty("bryan1.password", "12345678");
        testContext.getLoginPage().login(username, password);
    }
}
