package com.bguerradev.qa.utilities;

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
        loadFromFile("config.properties.example");
        loadFromFile("config.properties");
    }

    private static void loadFromFile(String resource) {
        InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(resource);
        if (inputStream != null) {
            try {
                props.load(inputStream);
            } catch (IOException e) {
                System.err.println("Failed to load " + resource + " → " + e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private static String determineEnvironment() {
        String env = System.getProperty("env");
        if (env != null && !env.isBlank()) return env.trim().toLowerCase();
        return props.getProperty("env", DEFAULT_ENV).trim().toLowerCase();
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        String envKey = CURRENT_ENV + "." + key;
        value = props.getProperty(envKey);
        if (value != null && !value.isBlank()) return value.trim();

        value = props.getProperty(key);
        if (value != null && !value.isBlank()) return value.trim();

        if (key.contains("url")) {
            if (key.equals("base.url.login")) return "https://smspy-frontend-pre.onrender.com/";
            if (key.equals("base.url.messages")) return "https://smspy-frontend-pre.onrender.com/messages";
            if (key.equals("base.url.messages.send")) return "https://smspy-frontend-pre.onrender.com/messages/send";
            if (key.equals("base.url.profile")) return "https://smspy-frontend-pre.onrender.com/profile";
            if (key.equals("api.base.url")) return "https://smspy-backend-pre.onrender.com/api/";
        }

        return defaultValue;
    }

    public static Properties getAllProperties() {
        return props;
    }
}
