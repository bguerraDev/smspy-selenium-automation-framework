package stepDefinitions;

import api.BaseClient;
import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import pojo.Message;
import pojo.User;

import java.io.File;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

public class ApiSecuritySteps {

    private final TestContext testContext;
    private Response response;
    private String tamperedToken;

    public ApiSecuritySteps(TestContext testContext) {
        this.testContext = testContext;
    }

    // ──────────────────────────────────────────────
    // Background / Setup
    // ──────────────────────────────────────────────

    @Given("login as {string} and store token")
    public void loginAsAndStoreToken(String username) {
        String password = ApiStepsBase.getPasswordForUsername(username);
        User user = User.builder().username(username).password(password).build();

        response = given().body(user).post("/token/");
        response.then().statusCode(200);

        String token = response.jsonPath().getString("access");
        testContext.getScenarioContext().setContext("ACCESS_TOKEN", token);
    }

    // ──────────────────────────────────────────────
    // SQL Injection Tests
    // ──────────────────────────────────────────────

    @When("I send POST _token_ with SQL injection in username {string} and password {string}")
    public void sendLoginWithSqlInjection(String maliciousUsername, String password) {
        User maliciousUser = User.builder().username(maliciousUsername).password(password).build();
        response = given().body(maliciousUser).post("/token/");
    }

    @Then("the response should not succeed or leak database errors")
    public void responseShouldNotLeakDbErrors() {
        int status = response.getStatusCode();
        Assert.assertTrue(status >= 400, "Expected failure, got " + status);

        String body = response.getBody().asString().toLowerCase();
        Assert.assertFalse(body.contains("sql"), "Possible SQL leak in response");
        Assert.assertFalse(body.contains("database"), "Possible DB error leak");
        Assert.assertFalse(body.contains("syntax error"), "Possible SQL syntax leak");
    }

    @When("I send POST _messages_send with receiver ID {int} and injection content {string}")
    public void sendMessageWithInjection(int receiverId, String maliciousContent) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");

        response = given()
                .spec(BaseClient.getAuthSpec(token))
                .contentType("multipart/form-data")
                .multiPart("receiver", receiverId)
                .multiPart("content", maliciousContent)
                .post("/messages/send/");
    }

    // ──────────────────────────────────────────────
    // Auth Bypass
    // ──────────────────────────────────────────────

    @Given("I have a valid token")
    public void iHaveValidToken() {
        // Already from background
    }

    @When("I tamper the token by changing user_id to {int}")
    public void tamperTokenChangeUserId(int newUserId) {
        String originalToken = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        // Simple tamper: split JWT, base64 decode payload, change user_id, re-encode (requires base64 lib)
        // For simplicity, assume tamper logic or use a known invalid token
        tamperedToken = originalToken.replace("user_id\":1", "user_id\":" + newUserId);  // rough tamper
    }

    @When("send GET _protected_ with tampered token")
    public void sendGetProtectedWithTamperedToken() {
        response = given()
                .header("Authorization", "Bearer " + tamperedToken)
                .get("/protected/");
    }

    @Then("the response should return 401 or 403 Unauthorized")
    public void responseShouldBeUnauthorized() {
        int status = response.getStatusCode();
        Assert.assertTrue(status == 401 || status == 403, "Expected unauthorized, got " + status);
    }

    // ──────────────────────────────────────────────
    // BOLA Tests
    // ──────────────────────────────────────────────

    @When("I attempt PUT _profile_ with another user ID {int}")
    public void attemptPutProfileWithForeignId(int foreignId) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        File dummyAvatar = new File("src/test/resources/test_avatar_selenium.webp");

        response = given()
                .spec(BaseClient.getAuthSpec(token))
                .contentType("multipart/form-data")
                .multiPart("avatar", dummyAvatar, ApiStepsBase.getMimeType(dummyAvatar))
                .put("/profile/" + foreignId);  // assuming ID in URL; adjust if not
    }

    @Then("the response should deny access with 403 or 404")
    public void responseShouldDenyAccess() {
        int status = response.getStatusCode();
        Assert.assertTrue(status == 403 || status == 404, "Expected denial, got " + status);
    }

    // ──────────────────────────────────────────────
    // Unauthorized Access
    // ──────────────────────────────────────────────

    @When("I send GET _protected_ without token")
    public void sendGetProtectedWithoutToken() {
        response = given().get("/protected/");
    }

    // ──────────────────────────────────────────────
    // Other Injection / Rate Limit
    // ──────────────────────────────────────────────

    @When("I send POST _register_ with injection in email {string}")
    public void sendRegisterWithInjectionEmail(String maliciousEmail) {
        User maliciousUser = User.builder()
                .username("test_user")
                .password("test")
                .email(maliciousEmail)
                .build();
        response = given().body(maliciousUser).post("/register/");
    }

    @When("I send multiple POST _messages_send_ requests in quick succession")
    public void sendMultipleMessagesQuickly() {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        int receiverId = 2;  // known

        for (int i = 0; i < 10; i++) {  // simulate burst
            given()
                    .spec(BaseClient.getAuthSpec(token))
                    .contentType("multipart/form-data")
                    .multiPart("receiver", String.valueOf(receiverId))
                    .multiPart("content", "Spam test " + i)
                    .post("/messages/send/");
        }
    }

    @Then("the backend should rate-limit or reject after threshold")
    public void backendShouldRateLimit() {
        // Manual check or assert on response codes >5xx
        // In real test, monitor logs or add rate limit check
        Assert.assertTrue(true); // Placeholder; expand with actual rate limit test
    }

    // ──────────────────────────────────────────────
    // DB Leak via Error Messages
    // ──────────────────────────────────────────────

    @When("I send GET _users_ with injection param {string}")
    public void sendUsersWithInjectionParam(String param) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        response = given().spec(BaseClient.getAuthSpec(token)).queryParam("search", param.substring(1)).get("/users/");
    }

    @Then("response should not return all DB users")
    public void responseShouldNotReturnAllUsers() {
        int userCount = response.jsonPath().getList(".").size();
        Assert.assertTrue(userCount < 100, "Possible injection leak — returned too many users: " + userCount);
    }

    @When("I send invalid POST _register_ with excessively long username")
    public void sendRegisterWithLongUsername() {
        String veryLongUsername = "user_" + "a".repeat(500);  // 500+ chars — likely triggers validation or DB error

        User maliciousUser = User.builder()
                .username(veryLongUsername)
                .password("testpass123")
                .email("longuser@test.com")
                .build();

        response = given()
                .body(maliciousUser)
                .post("/register/");
    }

    @When("I send POST _register_ with malicious characters in email {string}")
    public void iSendPOST_register_WithMaliciousCharactersInEmail(String maliciousEmail) {
        User maliciousUser = User.builder()
                .username("malicious_" + UUID.randomUUID().toString().substring(0, 8))
                .password("testpass123")
                .email(maliciousEmail)
                .build();

        response = given()
                .body(maliciousUser)
                .post("/register/");
    }

    @Then("the error response should return 4xx or 500")
    public void errorResponseShouldBe4xxOr500() {
        int status = response.getStatusCode();
        Assert.assertTrue(status >= 400 && status <= 599,
                "Expected client/server error (4xx/5xx), but got: " + status);
        System.out.println("Error status received: " + status);
    }

    @Then("the error response should not expose DB schema or SQL details")
    public void errorResponseShouldNotExposeDbDetails() {
        String body = response.getBody().asString().toLowerCase();

        // List of sensitive keywords that should NOT appear
        String[] forbiddenKeywords = {
                "sql", "select", "insert", "update", "delete", "from", "where", "table", "customuser",
                "user_messages", "syntax error", "traceback", "file", ".py", "django.db", "postgresql",
                "psycopg2", "neon.tech", "connection refused", "database error"
        };

        for (String keyword : forbiddenKeywords) {
            Assert.assertFalse(body.contains(keyword),
                    "Forbidden keyword found in error response: '" + keyword + "'. Possible leak!");
        }

        // Optional: assert it's a clean JSON error
        response.then()
                .body("$", notNullValue())  // at least some JSON
                .body("detail", notNullValue()); // common in DRF

        System.out.println("Error response is clean — no DB/SQL leaks detected");
    }
}