package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties props = new Properties();
    private static final String DEFAULT_ENV = "staging";
    private static final String CURRENT_ENV;

    static {
        loadProperties();
        CURRENT_ENV = determineEnvironment();
    }

    private static void loadProperties() {
        // Try to load config.properties (optional in CI)
        loadFromFile("config.properties.example");

        // Try to load secret (optional)
        loadFromFile("config.properties.secret");

        // If nothing loaded, it's OK — use -D or defaults
        if (props.isEmpty()) {
            System.out.println("No config.properties found — relying on -D system properties or defaults");
        }
    }

    private static void loadFromFile(String resource) {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (inputStream != null) {
                props.load(inputStream);
                System.out.println("Loaded config from classpath: " + resource);
            }
        } catch (IOException e) {
            System.err.println("Failed to load " + resource + " → " + e.getMessage());
        }
    }

    private static String determineEnvironment() {
        String env = System.getProperty("env");
        if (env != null && !env.isBlank()) {
            return env.trim().toLowerCase();
        }
        return props.getProperty("env", DEFAULT_ENV).trim().toLowerCase();
    }

    public static String getEnvironment() {
        return CURRENT_ENV;
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        // Highest priority: system property (-D from CI)
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        // Environment-prefixed
        String envKey = CURRENT_ENV + "." + key;
        value = props.getProperty(envKey);
        if (value != null && !value.isBlank()) return value.trim();

        // Plain key
        value = props.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        // Default (for CI safety)
        return defaultValue != null ? defaultValue : missingKeyException(key, envKey);
    }

    private static String missingKeyException(String key, String envKey) {
        throw new IllegalStateException(
                "Missing required configuration → key: '%s' (tried: '%s', '%s', plain '%s')"
                        .formatted(key, key, envKey, key));
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    // Add specific getters with CI-safe defaults
    public static String getApiBaseUrl() {
        return getProperty("api.base.url", "https://smspy-backend-pre.onrender.com/api/");
    }

    public static String getBaseUrlLogin() {
        return getProperty("base.url.login", "https://smspy-frontend-pre.onrender.com/");
    }

    public static String getBaseUrlMessages() {
        return getProperty("base.url.messages", "https://smspy-frontend-pre.onrender.com/messages");
    }

    public static String getBaseUrlMessagesSend() {
        return getProperty("base.url.messages.send", "https://smspy-frontend-pre.onrender.com/messages/send");
    }

    public static String getBaseUrlProfile() {
        return getProperty("base.url.profile", "https://smspy-frontend-pre.onrender.com/profile");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "true"));
    }

    public static String getBryan1Password() {
        return getProperty("bryan1.password", "dummy_password_for_ci");
    }
}