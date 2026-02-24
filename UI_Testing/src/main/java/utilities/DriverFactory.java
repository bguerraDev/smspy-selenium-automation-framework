package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    public static WebDriver createDriver(String browserName) {
        browserName = browserName.toLowerCase().trim();

        switch (browserName) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();

                // Core prefs to disable password manager & leak detection
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false); // explicit leak disable
                chromeOptions.setExperimentalOption("prefs", prefs);

                // Additional flags to kill popups & credential prompts
                chromeOptions.addArguments("--disable-infobars"); // no "Chrome is being controlled" bar
                chromeOptions.addArguments("--disable-notifications"); // no notifications
                chromeOptions.addArguments("--disable-popup-blocking"); // allow controlled popups
                chromeOptions.addArguments("--disable-save-password-bubble"); // kill save password popup
                chromeOptions.addArguments("--disable-blink-features=Autofill"); // disable autofill entirely
                chromeOptions.addArguments("--password-store=basic"); // force basic store (no sync)

                // Optional: Headless for CI
                if (ConfigReader.isHeadless()) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }

                return new ChromeDriver(chromeOptions);

            case "firefox":
                return new FirefoxDriver();

            case "edge":
                return new EdgeDriver();

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }
    }
}