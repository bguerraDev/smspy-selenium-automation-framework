package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilities.ConfigReader;
import io.qameta.allure.Step;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ComposeMessagePage extends BasePage {

    public ComposeMessagePage(WebDriver driver) {
        super(driver);
        verifyCurrentUrl(ConfigReader.getProperty(Constants.URL_MESSAGES_SEND));
    }

    @Step("Select receiver: {0}")
    public void selectReceiver(String receiver) {
        click(Constants.RECEIVER_SELECT);
        clickUserSelectComponent(receiver, Constants.RECEIVER_SELECT_LIST_USERS);
    }

    @Step("Enter message content")
    public void enterContent(String content) {
        type(Constants.INPUT_MESSAGE, content);
    }

    @Step("Upload image: {0}")
    public void uploadImage(String filePath) {
        uploadFile(filePath, Constants.INPUT_IMAGE, Constants.INPUT_IMAGE_SPAN);
    }

    @Step("Click Send button")
    public void clickSend() {
        click(Constants.BUTTON_FORM_SEND);
    }

    @Step("Click Come Back button")
    public void clickComeBack() {
        click(Constants.BUTTON_VOLVER);
    }

    @Step("Wait for toast message to disappear")
    public void waitToastMessageSentDisappears() {
        dismissToast(Constants.SPAN_MESSAGE_SENT, Constants.TOAST_CLOSE_BUTTON);
    }

}