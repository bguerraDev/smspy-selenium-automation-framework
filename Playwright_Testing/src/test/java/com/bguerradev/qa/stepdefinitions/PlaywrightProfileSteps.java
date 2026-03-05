package com.bguerradev.qa.stepdefinitions;

import com.bguerradev.qa.context.TestContext;
import com.bguerradev.qa.pages.ProfilePage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PlaywrightProfileSteps {
    private final TestContext testContext;

    public PlaywrightProfileSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I navigate to profile page")
    public void iNavigateToProfilePage() {
        testContext.getInboxPage().goToMyProfile();
    }

    @When("I upload new avatar {string}")
    public void iUploadNewAvatar(String filePath) {
        ProfilePage profilePage = testContext.getProfilePage();
        profilePage.uploadNewAvatar(filePath);
        profilePage.clickUpdateAvatar();
    }

    @Then("the profile avatar is updated")
    public void theProfileAvatarIsUpdated() {
        testContext.getProfilePage().waitForProfileUpdated();
    }
}
