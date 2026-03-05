package com.bguerradev.qa.stepdefinitions;

import com.bguerradev.qa.context.TestContext;
import com.bguerradev.qa.pages.*;
import com.bguerradev.qa.utilities.ConfigReader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class PlaywrightMessagingSteps {
    private final TestContext testContext;

    public PlaywrightMessagingSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Then("I am on the inbox page")
    public void iAmOnTheInboxPage() {
        Assert.assertTrue(testContext.getInboxPage().titleMessageIsDisplayed(), "Not on inbox page");
    }

    @When("I navigate to compose message page")
    public void iNavigateToComposeMessagePage() {
        testContext.getInboxPage().goToSendMessage();
    }

    @When("I select receiver {string} and type content {string}")
    public void iSelectReceiverAndTypeContent(String receiver, String content) {
        ComposeMessagePage composePage = testContext.getComposeMessagePage();
        composePage.selectReceiver(receiver);
        composePage.enterContent(content);
        composePage.clickSend();
    }

    @When("I select receiver {string} and type content {string} with image")
    public void iSelectReceiverAndTypeContentWithImage(String receiver, String content) {
        ComposeMessagePage composePage = testContext.getComposeMessagePage();
        composePage.selectReceiver(receiver);
        composePage.enterContent(content);
    }

    @When("I upload image {string}")
    public void iUploadImage(String filePath) {
        testContext.getComposeMessagePage().uploadImage(filePath);
        testContext.getComposeMessagePage().clickSend();
    }

    @Then("the message is sent successfully")
    public void theMessageIsSentSuccessfully() {
        ComposeMessagePage composePage = testContext.getComposeMessagePage();
        Assert.assertTrue(composePage.isSuccessMessageDisplayed(), "Success message 'Mensaje enviado' not displayed");
        composePage.clickVolver();
    }

    @Then("I should see the {string} in my inbox")
    public void iShouldSeeTheMessageInMyInbox(String message) {
        Assert.assertTrue(testContext.getInboxPage().isMessagePresent(message), "Message not found in inbox");
    }

    @When("I logout and login as {string} with receiver password")
    public void iLogoutAndLoginAsWithReceiverPassword(String username) {
        testContext.getInboxPage().goToMyProfile();
        testContext.getProfilePage().logOut();

        // Use receiver's password (e.g., bryan2.password)
        String passwordKey = username + ".password";
        String password = ConfigReader.getProperty(passwordKey, "12345678");
        testContext.getLoginPage().login(username, password);
    }
}
