package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;
import io.qameta.allure.Step;

public class InboxPage extends BasePage {

    private final By firstMessageReceived = By
            .cssSelector("//h3[contains(text(), 'Mensajes recibidos')]/following-sibling::div[1]");
    private final By firstMessageSent = By
            .cssSelector("//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div[1]");

    public InboxPage(WebDriver driver) {
        super(driver);
        verifyCurrentUrl(ConfigReader.getProperty(Constants.getUrlMessagesInbox()));
    }

    @Step("Check if message title is displayed")
    public boolean titleMessageIsDisplayed() {
        return isDisplayed(Constants.H2_TITLE_MESSAGE_LIST);
    }

    @Step("Go to Send Message page")
    public void goToSendMessage() {
        click(Constants.BUTTON_SEND_MESSAGE);
    }

    @Step("Go to My Profile")
    public void goToMyProfile() {
        click(Constants.BUTTON_MY_PROFILE);
    }

    @Step("Get first received message content")
    public String getFirstMessageContent() {
        return getText(firstMessageReceived);
    }

    @Step("Get first sent message recipient")
    public String getFirstMessageSender() {
        return getText(firstMessageSent);
    }

    /**
     * Checks if a message with the given content exists in received or sent list
     */
    @Step("Check if message with content '{0}' is present")
    public boolean isMessagePresent(String expectedContent) {
        // Search in received messages first
        By receivedLocator = By.xpath(
                "//h3[contains(text(), 'Mensajes recibidos')]/following-sibling::div//p[contains(text(), '"
                        + expectedContent + "')]");

        if (isDisplayed(receivedLocator)) {
            return true;
        }

        // Then check sent messages
        By sentLocator = By.xpath(
                "//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div" +
                        "//p[contains(text(), '" + expectedContent + "')]");

        return isDisplayed(sentLocator);
    }
}