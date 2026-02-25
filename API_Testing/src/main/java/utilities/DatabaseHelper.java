package utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String DB_URL = ConfigReader.getDbUrl();
    private static final String DB_USER = ConfigReader.getDbUser();
    private static final String DB_PASS = ConfigReader.getDbPassword();

    public static List<String> getTestableUsernames() {
        List<String> testableUsernames = new ArrayList<>();

        String sql = "SELECT username FROM user_messages_customuser ORDER BY username";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String configKey = username.replace(" ", "_");

                // Rule 1
                if (username.startsWith("user_")) {
                    testableUsernames.add(username);
                    continue;
                }

                // Rule 2
                String configuredPassword = ConfigReader.getOptionalProperty("test.user." + configKey + ".password");
                if (configuredPassword != null) {
                    testableUsernames.add(username);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch testable usernames from Neon DB", e);
        }

        return testableUsernames;
    }
}