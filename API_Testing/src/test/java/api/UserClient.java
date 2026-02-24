package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import pojo.User;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Login via API with username: {0}")
    public static Response login(String username, String password) {
        User loginUser = User.builder()
                .username(username)
                .password(password)
                .build();

        return given()
                .spec(baseSpec)
                .body(loginUser)
                .post("/token/");
    }
}
