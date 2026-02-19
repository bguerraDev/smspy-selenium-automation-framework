package pages;

import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.getProperty(Constants.URL_BASE));
        verifyCurrentUrl(ConfigReader.getProperty(Constants.URL_BASE));
        return this;
    }

    /**
     * USERNAME IS UNIQUE
     *
     * @param username
     */
    public LoginPage enterUsername(String username) {
        type(Constants.INPUT_USERNAME, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(Constants.INPUT_PASSWORD, password);
        return this;
    }

    public void clickLogin() {
        click(Constants.BUTTON_LOGIN);
    }

    public void login(String username, String password) {
        this.
                enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }
}