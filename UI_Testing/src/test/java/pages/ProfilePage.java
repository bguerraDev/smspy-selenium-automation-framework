package pages;

import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;

public class ProfilePage extends BasePage {

    public ProfilePage(WebDriver driver) {
        super(driver);
        verifyCurrentUrl(ConfigReader.getProperty(Constants.URL_PROFILE));
    }

    public void uploadNewAvatar(String filePath) {
        uploadFile(filePath, Constants.UPLOAD_IMAGE_AVATAR, Constants.UPLOAD_IMAGE_AVATAR_SPAN);
    }

    public void clickUpdateAvatar() {
        click(Constants.BUTTON_UPDATE_PROFILE_AVATAR);
    }

    public void goToMyMessages() {
        click(Constants.BUTTON_GO_TO_MY_MESSAGES);
    }

    public void logOut() {
        click(Constants.BUTTON_LOGOUT);
    }
}