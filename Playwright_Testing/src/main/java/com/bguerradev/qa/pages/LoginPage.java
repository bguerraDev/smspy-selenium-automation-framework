package com.bguerradev.qa.pages;

import com.microsoft.playwright.Page;

import io.qameta.allure.Step;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    @Step("Open Login Page")
    public void open(String url) {
        page.navigate(url);
    }

    @Step("Enter username: {0}")
    public void enterUsername(String username) {
        // Selenium used input[type='text']
        page.locator("input[type='text']").fill(username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        // Selenium used input[type='password']
        page.locator("input[type='password']").fill(password);
    }

    @Step("Click Login button")
    public void clickLogin() {
        // Selenium used button[type='submit']
        // The user mentioned "Ingresar", but let's be flexible or use the type
        page.locator("button[type='submit']").click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
