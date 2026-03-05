package com.bguerradev.qa.playwright;

import com.microsoft.playwright.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class PlaywrightFactory {
    private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
    private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

    public static Page initPage(Properties prop) {
        String browserName = prop.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(prop.getProperty("headless", "true"));

        tlPlaywright.set(Playwright.create());

        Browser browser;
        switch (browserName) {
            case "firefox":
                browser = tlPlaywright.get().firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "safari":
            case "webkit":
                browser = tlPlaywright.get().webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                break;
            case "chrome":
            default:
                browser = tlPlaywright.get().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(headless));
                break;
        }

        tlBrowser.set(browser);
        tlContext.set(browser.newContext(new Browser.NewContextOptions().setViewportSize(1920, 1080)));
        tlPage.set(tlContext.get().newPage());

        return tlPage.get();
    }

    public static Playwright getPlaywright() {
        return tlPlaywright.get();
    }

    public static Browser getBrowser() {
        return tlBrowser.get();
    }

    public static BrowserContext getContext() {
        return tlContext.get();
    }

    public static Page getPage() {
        return tlPage.get();
    }

    public static void quit() {
        if (tlPage.get() != null) tlPage.get().close();
        if (tlContext.get() != null) tlContext.get().close();
        if (tlBrowser.get() != null) tlBrowser.get().close();
        if (tlPlaywright.get() != null) tlPlaywright.get().close();
        
        tlPage.remove();
        tlContext.remove();
        tlBrowser.remove();
        tlPlaywright.remove();
    }
}
