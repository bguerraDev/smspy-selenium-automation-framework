package stepDefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.Constants;
import pages.InboxPage;
import pages.LoginPage;
import pages.ProfilePage;

public class UiProfileSteps {

    private final TestContext testContext;
    private ProfilePage profilePage;
    private InboxPage inboxPage;

    public UiProfileSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I navigate to profile page")
    public void iNavigateToProfilePage() {
        inboxPage = testContext.getInboxPage();
        inboxPage.goToMyProfile();
        profilePage = testContext.getProfilePage();
    }

    @When("I upload new avatar {string}")
    public void iUploadNewAvatar(String filePath) {
        profilePage.uploadNewAvatar(filePath);
        profilePage.clickUpdateAvatar();
    }

    @Then("the profile avatar is updated")
    public void theProfileAvatarIsUpdated() {
        Assert.assertTrue(profilePage.isDisplayed(Constants.SPAN_AVATAR_UPDATED));
    }
}