package stepDefinitions;

import context.ScenarioContext;
import utilities.ConfigReader;

import java.io.File;
import java.util.List;

/**
 * Helper methods for STEPS
 */
public class ApiStepsBase {

    public static List<String> getTestableUsernamesFromContext(ScenarioContext scenarioContext) {
        return (List<String>) scenarioContext.getContext("LIVE_TESTABLE_USERNAMES");
    }

    public static String getPasswordForUsername(String username) {
        if (username.startsWith("user_")) {
            return ConfigReader.getProperty("test.user.general.password");
        }
        String configKey = username.replace(" ", "_");
        String password = ConfigReader.getOptionalProperty("test.user." + configKey + ".password");
        if (password == null) {
            throw new IllegalStateException("No password configured for user: " + username);
        }
        return password;
    }

    public static String getMimeType(File image) {
        return image.getName().endsWith(".webp") ? "image/webp" :
                image.getName().endsWith(".png") ? "image/png" :
                        "image/jpeg";
    }
}
