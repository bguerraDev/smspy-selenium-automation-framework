package context;

import org.openqa.selenium.WebDriver;
import pages.ComposeMessagePage;
import pages.InboxPage;
import pages.LoginPage;
import pages.ProfilePage;

import java.time.Duration;

public class TestContext {

    // ThreadLocal to ensure each thread (each scenario in parallel) has its own
    // driver
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Page objects – lazy initialized per thread
    private LoginPage loginPage;
    private InboxPage inboxPage;
    private ComposeMessagePage composeMessagePage;
    private ProfilePage profilePage;

    /**
     * Initializes the driver for the current thread.
     * Called from Hooks @Before
     */
    public void initializeDriver(WebDriver webDriver) {
        if (webDriver == null) {
            throw new IllegalArgumentException("WebDriver cannot be null");
        }
        driverThreadLocal.set(webDriver);
        driverThreadLocal.get().manage().window().maximize();
        driverThreadLocal.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driverThreadLocal.get();

        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized for this thread. Call initializeDriver() first.");
        }
        return webDriver;
    }

    /**
     * Removes the driver from the current thread
     * Called from Hooks @After
     */
    public void removeDriver() {
        WebDriver webDriver = driverThreadLocal.get();
        if (webDriver != null) {
            try {
                webDriver.quit(); // close browser
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            driverThreadLocal.remove(); // clear ThreadLocal reference
        }
        // Clear page objects too
        loginPage = null;
        inboxPage = null;
        composeMessagePage = null;
        profilePage = null;
    }

    public LoginPage getLoginPage() {
        // Lazy initialization pattern
        if (loginPage == null)
            loginPage = new LoginPage(getDriver());
        return loginPage;
    }

    public InboxPage getInboxPage() {
        if (inboxPage == null)
            inboxPage = new InboxPage(getDriver());
        return inboxPage;
    }

    public ComposeMessagePage getComposeMessagePage() {
        if (composeMessagePage == null)
            composeMessagePage = new ComposeMessagePage(getDriver());
        return composeMessagePage;
    }

    public ProfilePage getProfilePage() {
        if (profilePage == null)
            profilePage = new ProfilePage(getDriver());
        return profilePage;
    }
}
