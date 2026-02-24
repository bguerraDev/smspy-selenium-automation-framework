package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.io.File;

import static io.restassured.RestAssured.given;

public class MessageClient extends BaseClient {

    @Step("Send message via API to receiverId '{1}' with content: {2}")
    public static Response sendMessage(String token, Integer receiverId, String content) {
        return given()
                .spec(getAuthSpec(token))
                .contentType("multipart/form-data")
                .multiPart("receiver", receiverId.toString())
                .multiPart("content", content)
                .post("/messages/send/");
    }

    @Step("Send message with image via API to receiverId '{1}' with content: {2}")
    public static Response sendMessageWithImage(String token, Integer receiverId, String content, File image,
            String mimeType) {
        return given()
                .spec(getAuthSpec(token))
                .contentType("multipart/form-data")
                .multiPart("receiver", receiverId.toString())
                .multiPart("content", content)
                .multiPart("image", image, mimeType)
                .post("/messages/send/");
    }

    @Step("Get messages list from endpoint: {1}")
    public static Response getMessages(String token, String endpoint) {
        return given()
                .spec(getAuthSpec(token))
                .get(endpoint);
    }
}
