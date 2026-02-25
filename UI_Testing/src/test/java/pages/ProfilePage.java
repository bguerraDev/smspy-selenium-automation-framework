package pages;

import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;
import io.qameta.allure.Step;

public class ProfilePage extends BasePage {

    public ProfilePage(WebDriver driver) {
        super(driver);
        verifyCurrentUrl(ConfigReader.getProperty(BasePage.getProfileUrl()));
    }

    @Step("Upload new avatar: {0}")
    public void uploadNewAvatar(String filePath) {
        uploadFile(filePath, Constants.UPLOAD_IMAGE_AVATAR, Constants.UPLOAD_IMAGE_AVATAR_SPAN);
    }

    @Step("Click Update Avatar button")
    public void clickUpdateAvatar() {
        click(Constants.BUTTON_UPDATE_PROFILE_AVATAR);
    }

    @Step("Go to My Messages")
    public void goToMyMessages() {
        click(Constants.BUTTON_GO_TO_MY_MESSAGES);
    }

    @Step("Log out")
    public void logOut() {
        click(Constants.BUTTON_LOGOUT);
    }
}