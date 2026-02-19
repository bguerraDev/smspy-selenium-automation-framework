package stepDefinitions;

import api.BaseClient;
import context.ScenarioContext;
import context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import pojo.TokenResponse;
import pojo.User;
import utilities.ConfigReader;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * TestContext holds the single, shared, thread-local ScenarioContext
 * Hooks and ApiSteps now both use the same TestContext instance
 * setContext in hook writes to the shared map
 * getContext in step reads from the same map → key is found → no null (list of users in db)
 */
public class ApiAuthSteps {

    /**
     * TestContext for achieve the get data from neon database. Problem with initialize the methods in hooks
     */
    private final TestContext testContext;
    private final ScenarioContext scenarioContext;
    private Response response;
    private User currentUser;
    private RequestSpecification reqSpec;
    public final static String RESPONSE_MESSAGE_USER_CREATED = "Usuario registrado con éxito.";

    // PicoContainer injection
    public ApiAuthSteps(TestContext testContext) {
        this.testContext = testContext;
        this.scenarioContext = testContext.getScenarioContext(); // get shared instance
        this.reqSpec = BaseClient.baseSpec; // Default to base spec
    }

    @Given("I have a new user payload with dynamic username")
    public void i_have_a_new_user_payload_with_dynamic_username() {
        String randomUser = "user_" + UUID.randomUUID().toString().substring(0, 4);
        currentUser = User.builder()
                .username(randomUser)
                .password(ConfigReader.getProperty("test.user.general.password"))
                .email(randomUser + "@test.com")
                .build();
        this.scenarioContext.setContext("REGISTER_USER", currentUser);
    }

    @When("I send a POST request to {string}")
    public void i_send_a_post_request_to(String endpoint) {
        response = given()
                .spec(reqSpec)
                .body(currentUser)
                .when()
                .post(endpoint);

        // DEBUG: always print
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asPrettyString());
    }

    @When("I send a POST request to {string} with the newly registered credentials")
    public void i_send_a_post_request_to_with_the_newly_registered_credentials(String endpoint) {
        User newUser = (User) this.scenarioContext.getContext("REGISTER_USER");

        response = given()
                .spec(BaseClient.baseSpec)
                .body(newUser)  // same username + password used in register
                .when()
                .post(endpoint);
    }

    @And("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        response.then().statusCode(statusCode);
    }


    @Then("the response should contain the success message")
    public void the_response_should_contain_the_success_message() {
        String actualMessage = response.jsonPath().getString("message");

        Assert.assertNotNull(actualMessage, "Response should have a 'message' field");
        Assert.assertEquals(actualMessage, RESPONSE_MESSAGE_USER_CREATED,
                "Expected success message but got: " + actualMessage);

        System.out.println("Register success message verified: " + actualMessage);
    }

    @Given("I have valid credentials for user {string}")
    public void i_have_a_valid_credentials_for_user(String username) {
        String usernameProperty = ConfigReader.getProperty("test.user." + username + "." + "username");
        String password = ConfigReader.getProperty("test.user." + username + "." + "password");
        currentUser = User.builder()
                .username(usernameProperty)
                .password(password)
                .build();
    }

    @When("I send a POST request to {string} with those credentials")
    public void i_send_a_post_request_to_with_those_credentials(String endpoint) {
        response = given()
                .spec(reqSpec)
                .body(currentUser)
                .when()
                .post(endpoint);
    }

    @Then("the response should contain valid access and refresh tokens")
    public void the_response_should_contain_valid_access_and_refresh_tokens() {
        TokenResponse tokenResponse = response.as(TokenResponse.class);
        Assert.assertNotNull(tokenResponse.getAccess(), "Access token should not be null");
        Assert.assertNotNull(tokenResponse.getRefresh(), "Refresh token should not be null");

        // Store for later steps
        this.scenarioContext.setContext("ACCESS_TOKEN", tokenResponse.getAccess());
        System.out.println("User logged in: " + currentUser.getUsername());
        System.out.println("Token: " + tokenResponse.getAccess());
    }

    /**
     * Steps for Login with all live users from database
     */

    @Given("I have the list of live usernames from database")
    public void iHaveTheListOfLiveUsernamesFromDatabase() {
        List<String> users = ApiStepsBase.getTestableUsernamesFromContext(this.scenarioContext);
        Assert.assertFalse(users.isEmpty(), "No testable users found in database");
    }

    @When("I attempt login for each live user")
    public void iAttemptLoginForEachLiveUser() {
        List<String> usernames = (List<String>) this.scenarioContext.getContext("LIVE_TESTABLE_USERNAMES");

        for (String username : usernames) {
            String password = ApiStepsBase.getPasswordForUsername(username);

            User loginUser = User.builder()
                    .username(username)
                    .password(password)
                    .build();

            Response loginResponse = given()
                    .spec(BaseClient.baseSpec)
                    .body(loginUser)
                    .post("/token/");

            loginResponse.then()
                    .statusCode(200)
                    .body("access", notNullValue())
                    .body("refresh", notNullValue());

            System.out.println("Successful login for user: " + username);
        }
    }

    @Then("all logins should succeed with valid tokens")
    public void allLoginsShouldSucceedWithValidTokens() {
        // If we reached here without failing, all logins succeeded
        System.out.println("All testable users logged in successfully");
    }
}
