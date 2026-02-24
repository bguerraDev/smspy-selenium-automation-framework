package stepDefinitions;

import api.BaseClient;
import api.MessageClient;
import api.UserClient;
import context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;
import pojo.Message;
import pojo.User;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;

public class ApiMessagingSteps {

    private final TestContext testContext;
    private Response response;

    public ApiMessagingSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    // ──────────────────────────────────────────────
    // Background / Shared Setup
    // ──────────────────────────────────────────────

    @Given("login as sender {string}")
    public void loginAsSender(String username) {
        loginAsUser(username);
    }

    @Given("login as receiver {string}")
    public void loginAsReceiver(String username) {
        loginAsUser(username);
    }

    @Given("login as {string}")
    public void loginAsUser(String username) {
        String password = ApiStepsBase.getPasswordForUsername(username);

        response = UserClient.login(username, password);

        response.then().statusCode(200);
        String token = response.jsonPath().getString("access");
        testContext.getScenarioContext().setContext("ACCESS_TOKEN", token);
        testContext.getScenarioContext().setContext("CURRENT_USERNAME", username);

        System.out.println("Logged in as: " + username + " | Token stored");
    }

    // ──────────────────────────────────────────────
    // Send Message Scenarios
    // ──────────────────────────────────────────────

    @Given("I have receiver {string} with ID {int}")
    public void iHaveReceiverWithID(String receiverUsername, int receiverId) {
        testContext.getScenarioContext().setContext("RECEIVER_USERNAME", receiverUsername);
        testContext.getScenarioContext().setContext("RECEIVER_ID", receiverId);
    }

    // ──────────────────────────────────────────────
    // Send Message
    // ──────────────────────────────────────────────

    @When("I send a message to receiver with content {string}")
    public void iSendMessageWithContent(String content) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        Integer receiverId = (Integer) testContext.getScenarioContext().getContext("RECEIVER_ID");

        response = MessageClient.sendMessage(token, receiverId, content);

        response.then()
                .statusCode(201)
                .body("message", equalTo("Mensaje enviado correctamente"));

        Integer messageId = response.jsonPath().getInt("data.id");
        testContext.getScenarioContext().setContext("LAST_MESSAGE_ID", messageId);
    }

    @When("I send a message with image to receiver")
    public void iSendMessageWithImage() {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        Integer receiverId = (Integer) testContext.getScenarioContext().getContext("RECEIVER_ID");

        File image = new File("src/test/resources/image_message_placeholder.webp");

        response = MessageClient.sendMessageWithImage(token, receiverId, "Test message with image", image,
                ApiStepsBase.getMimeType(image));

        response.then()
                .statusCode(201)
                .body("message", equalTo("Mensaje enviado correctamente"));

        String responseImageUrl = response.jsonPath().getString("data.image");
        testContext.getScenarioContext().setContext("LAST_IMAGE_URL", responseImageUrl);
    }

    @Then("the message should be successfully sent")
    public void theMessageShouldBeSuccessfullySent() {
        response.then()
                .body("message", equalTo("Mensaje enviado correctamente"))
                .body("data.id", notNullValue());
    }

    @Then("the response should contain a valid S3 image URL")
    public void responseContainsValidS3ImageUrl() {
        String imageUrl = (String) testContext.getScenarioContext().getContext("LAST_IMAGE_URL");
        Assert.assertNotNull(imageUrl);
        Assert.assertTrue(imageUrl.startsWith("https://smspy-media-pre.s3.eu-central-1.amazonaws.com/"));
    }

    // ──────────────────────────────────────────────
    // Receive / Verify
    // ──────────────────────────────────────────────

    @Given("I lookup receiver {string} ID from users list")
    public void lookupReceiverId(String receiverUsername) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");

        Response usersResp = given()
                .spec(BaseClient.getAuthSpec(token))
                .get("/users/");

        usersResp.then().statusCode(200);

        // Find ID by username
        Integer receiverId = usersResp.jsonPath()
                .getInt("find { it.username == '" + receiverUsername + "' }.id");

        Assert.assertNotNull(receiverId, "Receiver '" + receiverUsername + "' not found in users list");

        testContext.getScenarioContext().setContext("RECEIVER_ID", receiverId);
        testContext.getScenarioContext().setContext("RECEIVER_USERNAME", receiverUsername);
    }

    @When("I send a GET request to {string}")
    public void iSendGetRequestTo(String endpoint) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");

        response = MessageClient.getMessages(token, endpoint);
    }

    @Then("I should see the message I just sent in the list")
    public void iShouldSeeTheMessageInTheList() {
        Integer expectedMessageId = (Integer) testContext.getScenarioContext().getContext("LAST_MESSAGE_ID");
        Integer getResponseMessageId = response.jsonPath().getList(".", Message.class).get(0).getId();

        Assert.assertEquals(getResponseMessageId, expectedMessageId);

    }

    // ──────────────────────────────────────────────
    // Profile Update
    // ──────────────────────────────────────────────

    @When("I update my profile avatar with new image")
    public void iUpdateMyProfileAvatarWithNewImage() {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");
        File newAvatar = new File("src/test/resources/test_avatar_selenium.webp");

        response = given()
                .spec(BaseClient.getAuthSpec(token))
                .contentType("multipart/form-data")
                .multiPart("avatar", newAvatar, ApiStepsBase.getMimeType(newAvatar))
                .put("/profile/");

        response.then().statusCode(200);

        String newUrl = response.jsonPath().getString("data.avatar");
        testContext.getScenarioContext().setContext("NEW_AVATAR_URL", newUrl);
    }

    @Then("the user profile should show the new avatar URL")
    public void theUserProfileShouldShowTheNewAvatarUrl() {
        String expectedUrl = (String) testContext.getScenarioContext().getContext("NEW_AVATAR_URL");
        Assert.assertNotNull(expectedUrl);
        Assert.assertTrue(expectedUrl.startsWith("https://smspy-media-pre.s3.eu-central-1.amazonaws.com/"));
    }

    @Then("I should see a message from {string} with content {string} as the most recent in the list")
    public void iShouldSeeMessageFromSenderWithContentAsTheMostRecentInTheList(String expectedSender,
            String expectedContent) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");

        response = given()
                .spec(BaseClient.getAuthSpec(token))
                .get("/messages/received/");

        response.then().statusCode(200);

        System.out.println("Received messages body: " + response.getBody().asPrettyString());

        List<Message> messages = response.jsonPath().getList(".", Message.class);

        Assert.assertFalse(messages.isEmpty(), "No messages received at all");

        // Get the most recent (first) message
        Message mostRecent = messages.get(0);

        Assert.assertEquals(mostRecent.getSender_username(), expectedSender,
                "Most recent message sender mismatch. Expected: " + expectedSender);

        Assert.assertEquals(mostRecent.getContent(), expectedContent,
                "Most recent message content mismatch. Expected: " + expectedContent);

        System.out.println("Verified most recent message from " + expectedSender + ": " + expectedContent);
    }

    /**
     * NEGATIVE SCENARIOS
     */

    @When("I send a message to invalid receiver ID {int} with content {string}")
    public void sendMessageToInvalidReceiver(int invalidReceiverId, String content) {
        String token = (String) testContext.getScenarioContext().getContext("ACCESS_TOKEN");

        response = given()
                .spec(BaseClient.getAuthSpec(token))
                .contentType("multipart/form-data")
                .multiPart("receiver", invalidReceiverId)
                .multiPart("content", content)
                .post("/messages/send/");

    }

    @Then("the response should return an error status")
    public void responseShouldReturnErrorStatus() {
        int status = response.getStatusCode();
        Assert.assertTrue(status >= 400 && status < 600,
                "Expected error status (4xx/5xx), but got: " + status);
        System.out.println("Error response received with status: " + status);
    }

    @When("I send a POST request to {string} without token with content {string}")
    public void sendMessageWithoutToken(String endpoint, String content) {
        response = given()
                .spec(BaseClient.baseSpec)
                .contentType("multipart/form-data")
                .multiPart("receiver", 2) // must exist
                .multiPart("content", content)
                .post(endpoint);
    }

    @Then("the response should return 401 Unauthorized")
    public void responseShouldReturn401Unauthorized() {
        response.then()
                .statusCode(401)
                .body(containsString("Authentication credentials were not provided."));
    }
}
