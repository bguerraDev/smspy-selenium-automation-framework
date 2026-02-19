package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utilities.ConfigReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ComposeMessagePage extends BasePage {

    public ComposeMessagePage(WebDriver driver) {
        super(driver);
        verifyCurrentUrl(ConfigReader.getProperty(Constants.URL_MESSAGES_SEND));
    }

    public void selectReceiver(String receiver) {
        click(Constants.RECEIVER_SELECT);
        clickUserSelectComponent(receiver, Constants.RECEIVER_SELECT_LIST_USERS);
    }

    public void enterContent(String content) {
        type(Constants.INPUT_MESSAGE, content);
    }

    public void uploadImage(String filePath) {
        uploadFile(filePath, Constants.INPUT_IMAGE, Constants.INPUT_IMAGE_SPAN);
    }

    public void clickSend() {
        click(Constants.BUTTON_FORM_SEND);
    }

    public void clickComeBack() {
        click(Constants.BUTTON_VOLVER);
    }

    public void waitToastMessageSentDisappears() {
        dismissToast(Constants.SPAN_MESSAGE_SENT, Constants.TOAST_CLOSE_BUTTON);
    }


}