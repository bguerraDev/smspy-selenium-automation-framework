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
        String resource = "config.properties";
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resource);
            }
            props.load(inputStream);
            System.out.println("Loaded config from classpath: " + resource);
        } catch (IOException e) {
            System.err.println("Failed to load config → " + e.getMessage());
            throw new RuntimeException("Cannot start tests without config.properties", e);
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
        // 1. Highest priority → explicit system property
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        // 2. Environment-prefixed property (most common usage)
        String envKey = CURRENT_ENV + "." + key;
        value = props.getProperty(envKey);
        if (value != null && !value.isBlank()) return value.trim();

        // 3. Fallback to plain key (global settings)
        value = props.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        // 4. Default value or fail-fast
        return defaultValue != null ? defaultValue :
                missingKeyException(key, envKey);
    }

    /**
     * Returns the property value or null if not found.
     * Does NOT throw exception.
     */
    public static String getOptionalProperty(String key) {
        // First try environment-prefixed
        String env = getProperty("env", ""); // safe, no throw if env missing
        String prefixed = env.isEmpty() ? key : env + "." + key;

        String value = props.getProperty(prefixed);
        if (value != null) {
            return value;
        }

        // Fallback to plain key
        return props.getProperty(key);
    }

    private static String missingKeyException(String key, String envKey) {
        throw new IllegalStateException(
                "Missing required configuration → key: '%s' (tried: '%s', '%s', plain '%s')"
                        .formatted(key, key, envKey, key));
    }


    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getBrowser() {
        return getProperty("browser");
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
}