package stepDefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.*;
import utilities.ConfigReader;

public class UiMessagingSteps {

    private final TestContext testContext;
    private LoginPage loginPage;
    private InboxPage inboxPage;
    private ComposeMessagePage composePage;
    private ProfilePage profilePage;

    public UiMessagingSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("I am on the inbox page")
    public void iAmOnTheInboxPage() {
        inboxPage = testContext.getInboxPage();
        Assert.assertTrue(inboxPage.titleMessageIsDisplayed(), "Not on inbox page");
    }

    @When("I navigate to compose message page")
    public void iNavigateToComposeMessagePage() {
        inboxPage = testContext.getInboxPage();
        inboxPage.goToSendMessage();
        composePage = testContext.getComposeMessagePage();
    }

    @When("I select receiver {string} and type content {string}")
    public void iSelectReceiverAndTypeContent(String receiver, String content) {
        composePage.selectReceiver(receiver);
        composePage.enterContent(content);
        composePage.clickSend();
    }

    @When("I select receiver {string} and type content {string} with image")
    public void iSelectReceiverAndTypeContentWithImage(String receiver, String content) {
        composePage.selectReceiver(receiver);
        composePage.enterContent(content);
    }

    @When("I upload image {string}")
    public void iUploadImage(String filePath) {
        composePage.uploadImage(filePath);
        composePage.clickSend();
    }

    @Then("the message is sent successfully")
    public void theMessageIsSentSuccessfully() {
        Assert.assertTrue(composePage.isDisplayed(Constants.SPAN_MESSAGE_SENT), "Success message not displayed");
    }

    @Then("I should see the {string} in my inbox")
    public void iShouldSeeTheMessageInMyInbox(String message) {
        inboxPage = testContext.getInboxPage();
        Assert.assertTrue(inboxPage.isMessagePresent(message), "Message not found in inbox");
    }

    @When("I logout and login as {string} with receiver password")
    public void iLogoutAndLoginAsWithReceiverPassword(String username) {
        composePage.waitToastMessageSentDisappears();
        composePage.clickComeBack();
        inboxPage.goToMyProfile();
        profilePage = testContext.getProfilePage();
        profilePage.logOut();

        loginPage = testContext.getLoginPage();
        String password = ConfigReader.getBryan1Password(); // TODO only for this scenario. Is not a good practice
        loginPage.login(username, password);
    }
}