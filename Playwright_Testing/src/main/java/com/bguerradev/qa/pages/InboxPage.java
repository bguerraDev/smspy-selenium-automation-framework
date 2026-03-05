package com.bguerradev.qa.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class InboxPage {
    private final Page page;

    public InboxPage(Page page) {
        this.page = page;
    }

    @Step("Check if message title is displayed")
    public boolean titleMessageIsDisplayed() {
        try {
            page.getByText("Mensajes", new Page.GetByTextOptions().setExact(false)).first().waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Go to Send Message page")
    public void goToSendMessage() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Enviar Mensaje")).click();
    }

    @Step("Go to My Profile")
    public void goToMyProfile() {
        page.getByText("Mi Perfil", new Page.GetByTextOptions().setExact(false)).first().click();
    }

    @Step("Check if message with content '{0}' is present")
    public boolean isMessagePresent(String expectedContent) {
        try {
            page.getByText(expectedContent).first().waitFor();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Logout")
    public void logout() {
        page.getByText("Cerrar sesión", new Page.GetByTextOptions().setExact(false)).first().click();
    }
}
