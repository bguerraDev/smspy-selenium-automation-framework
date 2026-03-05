package com.bguerradev.qa.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import java.nio.file.Paths;

public class ProfilePage {
    private final Page page;

    public ProfilePage(Page page) {
        this.page = page;
    }

    @Step("Upload new avatar: {0}")
    public void uploadNewAvatar(String filePath) {
        page.locator("input#avatar-upload").setInputFiles(Paths.get(filePath));
    }

    @Step("Click Update Avatar button")
    public void clickUpdateAvatar() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Actualizar")).click();
    }

    @Step("Wait for profile updated message")
    public void waitForProfileUpdated() {
        page.getByText("Perfil actualizado").first().waitFor();
    }

    @Step("Go to My Messages")
    public void goToMyMessages() {
        page.getByText("Ir a mis mensajes", new Page.GetByTextOptions().setExact(false)).first().click();
    }

    @Step("Log out")
    public void logOut() {
        page.getByText("Cerrar sesión", new Page.GetByTextOptions().setExact(false)).first().waitFor();
        page.getByText("Cerrar sesión", new Page.GetByTextOptions().setExact(false)).first().click();
    }
}
