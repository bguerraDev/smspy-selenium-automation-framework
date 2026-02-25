package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import io.qameta.allure.Step;
import utilities.ConfigReader;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement findElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement findElementPresenceToBeLocated(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected List<WebElement> findElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    @Step("Click on element: {0}")
    protected void click(By locator) {
        findElement(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Get text from {0}")
    protected String getText(By locator) {
        return findElement(locator).getText();
    }

    protected List<WebElement> getList(By locator) {
        return findElements(locator);
    }

    @Step("Click receiver user '{0}' in Dropdown component {1}")
    protected void clickUserSelectComponent(String receiver, By locator) {

        await()
                .atMost(20, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .until(() -> {
                    List<WebElement> options = findElements(locator);
                    return options.size() > 1;
                });

        List<WebElement> listUsers = findElements(locator);
        System.out.println("Dropdown options count: " + listUsers.size());
        boolean found = false;
        for (WebElement user : listUsers) {
            String userNameText = user.getText().trim();
            if (userNameText.equals(receiver)) {
                user.click();
                found = true;
                break;
            }
        }

        if (!found) {
            Assert.fail("Receiver '" + receiver + "' not found in dropdown. Available options: " +
                    listUsers.stream().map(WebElement::getText).toList());
        }
    }

    @Step("Verify current URL is: {expectedUrl}")
    protected void verifyCurrentUrl(String expectedUrl) {
        System.out.println("Waiting for URL to become: " + expectedUrl);
        System.out.println("Current URL before wait: " + driver.getCurrentUrl());

        await()
                .atMost(90, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .until(() -> {
                    String current = driver.getCurrentUrl();
                    System.out.println("Polling URL: " + current);
                    return current.equals(expectedUrl);
                });

        String finalUrl = driver.getCurrentUrl();
        System.out.println("Final URL after wait: " + finalUrl);
        Assert.assertEquals(finalUrl, expectedUrl,
                "Not on expected page. Expected: " + expectedUrl + " but was: " + finalUrl);
    }

    @Step("Upload file '{0}' to {1}")
    protected void uploadFile(String filePath, By locatorUploadSelector, By locatorSpanSelector) {
        // Locate the hidden input directly
        WebElement fileInput = findElementPresenceToBeLocated(locatorUploadSelector);

        // Send absolute path (very important!)
        String absolutePath = new File(filePath).getAbsolutePath();
        fileInput.sendKeys(absolutePath);

        System.out.println("File path sent directly to hidden input: " + absolutePath);

        // Optional: Wait for span to update (visual confirmation)
        wait.until(d -> {
            String spanText = driver.findElement(locatorSpanSelector)
                    .getText().trim();
            return spanText.contains(new File(filePath).getName());
        });

        System.out.println("File selection confirmed in UI");
    }

    public boolean isDisplayed(By locator) {
        try {
            return findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getLoginUrl() {
        return ConfigReader.getBaseUrlLogin();
    }

    public static String getMessagesInboxUrl() {
        return ConfigReader.getBaseUrlMessages();
    }

    public static String getMessagesSendUrl() {
        return ConfigReader.getBaseUrlMessagesSend();
    }

    public static String getProfileUrl() {
        return ConfigReader.getBaseUrlProfile();
    }

    /**
     * Tries to dismiss the toast by clicking the close button (✕).
     * If no button or click fails, falls back to waiting for invisibility.
     */
    protected void dismissToast(By toastLocator, By closeButtonLocator) {
        try {
            findElementToBeClickable(closeButtonLocator).click();
            System.out.println("Toast close button clicked — dismissed");
        } catch (Exception e) {
            System.out.println("Could not click toast close button: " + e.getMessage());
            // Fallback: wait for invisibility
            wait.until(ExpectedConditions.invisibilityOfElementLocated(toastLocator));
            System.out.println("Toast invisible after fallback wait");
        }
    }

    /**
     * FLAKINESS RESULT
     * Waits until toast is disappearing.
     * Handles React fade-out + removal.
     *
     * @param toastLocator      Locator of toast div
     * @param maxTimeoutSeconds Max wait (default 8s)
     */
    protected void waitForToastToDisappear(By toastLocator, int maxTimeoutSeconds) {
        await()
                .atMost(maxTimeoutSeconds, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .pollDelay(300, TimeUnit.MILLISECONDS)
                .ignoreExceptions()
                .until(() -> {
                    try {
                        WebElement toast = driver.findElement(toastLocator);
                        boolean visible = toast.isDisplayed();
                        click(Constants.TOAST_CLOSE_BUTTON);
                        System.out.println("Toast visible? " + visible + " | Location: " + toast.getLocation()
                                + " | Size: " + toast.getSize());
                        return !visible;
                    } catch (Exception e) {
                        System.out.println(
                                "Toast check exception: " + e.getClass().getSimpleName() + " — assuming disappeared");
                        return true;
                    }
                });

        System.out.println("Toast wait completed");
    }

    protected void dismissToastIfPresent(By closeButtonLocator) {
        if (isDisplayed(closeButtonLocator)) {
            click(closeButtonLocator);
            System.out.println("Toast manually dismissed");
        }
    }
}