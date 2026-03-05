package com.bguerradev.qa.hooks;

import com.bguerradev.qa.api.BaseClient;
import com.bguerradev.qa.playwright.PlaywrightFactory;
import com.bguerradev.qa.utilities.ConfigReader;
import com.bguerradev.qa.context.WarmUpBackend;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Hooks {
    private Page page;

    @Before(order = 0)
    public void warmUp() {
        WarmUpBackend.executeWithColdStartRetry(() ->
                given().spec(BaseClient.baseSpec).get("/protected/")
        );
    }

    @Before(order = 1)
    public void launchBrowser() {
        Properties prop = ConfigReader.getAllProperties();
        page = PlaywrightFactory.initPage(prop);
        PlaywrightFactory.getContext().tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
    }

    @After(order = 0)
    public void quitBrowser() {
        PlaywrightFactory.quit();
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            String screenshotName = scenario.getName().replaceAll(" ", "_");
            byte[] sourcePath = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            scenario.attach(sourcePath, "image/png", screenshotName);
            Allure.addAttachment(screenshotName, new ByteArrayInputStream(sourcePath));

            PlaywrightFactory.getContext().tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("target/playwright-traces/" + screenshotName + ".zip")));
        } else {
            PlaywrightFactory.getContext().tracing().stop(new Tracing.StopOptions()
                    .setPath(null));
        }
    }
}
