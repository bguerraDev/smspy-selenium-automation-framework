package listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import context.TestContext;
import io.qameta.allure.Attachment;

public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshotAndLog(result, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        attachScreenshotAndLog(result, "BROKEN/SKIPPED");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        attachScreenshotAndLog(result, "SOFT FAILED");
    }

    private void attachScreenshotAndLog(ITestResult result, String status) {
        try {
            WebDriver driver = TestContext.getDriver();
            if (driver != null) {
                saveScreenshot(driver, status + " - Screenshot (" + result.getName() + ")");
            }
        } catch (Exception e) {
            System.out.println(
                    "AllureListener: Could not capture screenshot. Driver may have been quit or not initialized. "
                            + e.getMessage());
        }

        saveTextLog(status + ": " + result.getMethod().getMethodName() + " - Throwable: " +
                (result.getThrowable() != null ? result.getThrowable().toString() : "No throwable"));
    }

    @Attachment(value = "{0}", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver, String title) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public String saveTextLog(String message) {
        return message;
    }
}