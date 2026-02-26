package context;

import api.BaseClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utilities.ConfigReader;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class WarmUpBackend {

    public static void wakeUpBackendRender() {
        String username = ConfigReader.getProperty("test.user.bryan1.username", "bryan1");
        String password = ConfigReader.getBryan1Password();
        String apiBaseUrl = ConfigReader.getApiBaseUrl();

        System.out.println("Warming up Render backend via full login flow simulation...");
        System.out.println("Using username: " + username);

        await()
                .atMost(180, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> {
                    try {
                        // 1. POST /token/
                        String loginUrl = apiBaseUrl + "token/";
                        System.out.println("Attempting warm-up login: POST " + loginUrl);
                        Response loginResp = given()
                                .contentType(ContentType.JSON)
                                .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                                .post(loginUrl);

                        if (loginResp.getStatusCode() != 200) {
                            System.out.println("Login warm-up failed (status " + loginResp.getStatusCode() + "), body: "
                                    + loginResp.getBody().asString());
                            return false;
                        }

                        String accessToken = loginResp.jsonPath().getString("access");
                        System.out.println("Login warm-up success. Token obtained.");

                        // 2. GET /messages/received/ (Auth required)
                        String msgUrl = apiBaseUrl + "messages/received/";
                        Response msgResp = given()
                                .spec(BaseClient.getAuthSpec(accessToken))
                                .get(msgUrl);

                        System.out.println("Auth endpoint " + msgUrl + " status: " + msgResp.getStatusCode());

                        // 3. Optional: GET /users/
                        String meUrl = apiBaseUrl + "users/";
                        Response meResp = given()
                                .spec(BaseClient.getAuthSpec(accessToken))
                                .get(meUrl);
                        System.out.println("Auth endpoint " + meUrl + " status: " + meResp.getStatusCode());

                        return msgResp.getStatusCode() == 200;
                    } catch (Exception e) {
                        System.out.println("Warm-up attempt failed with exception: " + e.getMessage());
                        return false;
                    }
                });
        System.out.println("Backend fully warmed up and authenticated flow verified.");
    }
}
