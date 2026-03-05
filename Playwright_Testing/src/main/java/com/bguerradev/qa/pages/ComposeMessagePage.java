package com.bguerradev.qa.pages;

import java.nio.file.Paths;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class ComposeMessagePage {
    private final Page page;

    public ComposeMessagePage(Page page) {
        this.page = page;
    }

    @Step("Select receiver: {0}")
    public void selectReceiver(String receiver) {
        page.locator("select").selectOption(receiver);
    }

    @Step("Enter message content: {0}")
    public void enterContent(String content) {
        page.locator("textarea").fill(content);
    }

    @Step("Upload image: {0}")
    public void uploadImage(String filePath) {
        // Playwright handles file uploads by setting files on the input element
        // directly
        page.locator("input#file-upload").setInputFiles(Paths.get(filePath));
    }

    @Step("Click Send button")
    public void clickSend() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Enviar")).first().click();
    }

    @Step("Check if success message is displayed")
    public boolean isSuccessMessageDisplayed() {
        try {
            // The message is transient (~4s). Using regex for maximum flexibility.
            page.locator("text=/Mensaje enviado/i").first()
                    .waitFor(new com.microsoft.playwright.Locator.WaitForOptions().setTimeout(20000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Click Volver button")
    public void clickVolver() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Volver")).click();
    }
}
