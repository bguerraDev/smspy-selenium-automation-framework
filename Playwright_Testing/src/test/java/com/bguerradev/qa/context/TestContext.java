package com.bguerradev.qa.context;

import com.bguerradev.qa.pages.*;
import com.bguerradev.qa.playwright.PlaywrightFactory;
import com.microsoft.playwright.Page;

public class TestContext {
    private LoginPage loginPage;
    private InboxPage inboxPage;
    private ComposeMessagePage composeMessagePage;
    private ProfilePage profilePage;

    public Page getPage() {
        return PlaywrightFactory.getPage();
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) loginPage = new LoginPage(getPage());
        return loginPage;
    }

    public InboxPage getInboxPage() {
        if (inboxPage == null) inboxPage = new InboxPage(getPage());
        return inboxPage;
    }

    public ComposeMessagePage getComposeMessagePage() {
        if (composeMessagePage == null) composeMessagePage = new ComposeMessagePage(getPage());
        return composeMessagePage;
    }

    public ProfilePage getProfilePage() {
        if (profilePage == null) profilePage = new ProfilePage(getPage());
        return profilePage;
    }
}
