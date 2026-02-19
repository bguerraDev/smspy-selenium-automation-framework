package utilities;

import lombok.Getter;

public enum UserCredentials {

    STANDARD("standard", "staging.username", "staging.password"),
    PROBLEM("problem", "uat.username", "uat.password"),
    PERFORMANCE_GLITCH("performance_glitch", "prod.username", "prod.password");

    @Getter
    private final String typeName;
    private final String usernameKey;
    private final String passwordKey;

    UserCredentials(String typeName, String usernameKey, String passwordKey) {
        this.typeName = typeName;
        this.usernameKey = usernameKey;
        this.passwordKey = passwordKey;
    }

    public String getUsername() {
        return ConfigReader.getProperty(usernameKey);
    }

    public String getPassword() {
        return ConfigReader.getProperty(passwordKey);
    }

    public static UserCredentials fromType(String type) {
        if (type == null) throw new IllegalArgumentException("User type cannot be null");

        for (UserCredentials user : values()) {
            if (user.typeName.equalsIgnoreCase(type.trim())) {
                return user;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }

}
