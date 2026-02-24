package utilities;

import net.bytebuddy.implementation.bytecode.Throw;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Arrays;
import java.util.List;

public class DriverFactory {

    public static WebDriver createDriver(String browserName) {
        // ChromeOptions chromeOptions = new ChromeOptions();
        // options.addArguments("--headless=new"); // uncomment for CI
        // options.addArguments("--disable-gpu");
        /*
         * return switch (browserName.toLowerCase().trim()) {
         * case "chrome" -> ChromeOptions
         * case "firefox" -> new FirefoxDriver();
         * case "edge" -> new EdgeDriver();
         * default ->
         * };
         */

        switch (browserName.toLowerCase().trim()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (ConfigReader.isHeadless()) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--disable-gpu");
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
