package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ConfigReader;

public class InboxPage extends BasePage {

    private final By firstMessageReceived = By.cssSelector("//h3[contains(text(), 'Mensajes recibidos')]/following-sibling::div[1]");
    private final By firstMessageSent = By.cssSelector("//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div[1]");

    public InboxPage(WebDriver driver) {
        super(driver);
        verifyCurrentUrl(ConfigReader.getProperty(Constants.URL_MESSAGES_INBOX));
    }

    public boolean titleMessageIsDisplayed() {
        return isDisplayed(Constants.H2_TITLE_MESSAGE_LIST);
    }

    public void goToSendMessage() {
        click(Constants.BUTTON_SEND_MESSAGE);
    }

    public void goToMyProfile() {
        click(Constants.BUTTON_MY_PROFILE);
    }

    public String getFirstMessageContent() {
        return getText(firstMessageReceived);
    }

    public String getFirstMessageSender() {
        return getText(firstMessageSent);
    }

    /**
     * Checks if a message with the given content exists in received or sent list
     */
    public boolean isMessagePresent(String expectedContent) {
        // Search in received messages first
        By receivedLocator = By.xpath(
                "//h3[contains(text(), 'Mensajes recibidos')]/following-sibling::div//p[contains(text(), '" + expectedContent + "')]"
        );

        if (isDisplayed(receivedLocator)) {
            return true;
        }

        // Then check sent messages
        By sentLocator = By.xpath(
                "//h3[contains(text(), 'Mensajes enviados')]/following-sibling::div" +
                        "//p[contains(text(), '" + expectedContent + "')]"
        );

        return isDisplayed(sentLocator);
    }
}