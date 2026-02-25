package pages;

import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;
import io.qameta.allure.Step;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Login Page")
    public LoginPage open() {
        driver.get(BasePage.getLoginUrl());
        verifyCurrentUrl(BasePage.getLoginUrl());
        return this;
    }

    /**
     * USERNAME IS UNIQUE
     *
     * @param username
     */
    @Step("Enter username: {0}")
    public LoginPage enterUsername(String username) {
        type(Constants.INPUT_USERNAME, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        logEnterPassword();
        type(Constants.INPUT_PASSWORD, password);
        return this;
    }

    @Step("Enter password (masked)")
    private void logEnterPassword() {
        // This method exists solely to add a step to the Allure report without exposing
        // the password
    }

    @Step("Click Login button")
    public void clickLogin() {
        click(Constants.BUTTON_LOGIN);
    }

    public void login(String username, String password) {
        this.enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }
}