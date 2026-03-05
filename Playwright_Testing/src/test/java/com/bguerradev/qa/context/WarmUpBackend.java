package com.bguerradev.qa.context;

import com.bguerradev.qa.utilities.ConfigReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class WarmUpBackend {
    public static void wakeUpBackendRender() {
        String username = ConfigReader.getProperty("test.user.bryan1.username", "bryan1");
        String password = ConfigReader.getProperty("bryan1.password", "12345678");
        String apiBaseUrl = ConfigReader.getProperty("api.base.url", "https://smspy-backend-pre.onrender.com/api/")
                + "token/";

        System.out.println("ApiBaseUrl: " + apiBaseUrl);

        System.out.println("Warming up Render backend for Playwright tests...");

        await()
                .atMost(180, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(() -> {
                    try {
                        Response loginResp = given()
                                .contentType(ContentType.JSON)
                                .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                                .post(apiBaseUrl);

                        if (loginResp.getStatusCode() != 200)
                            return false;

                        String accessToken = loginResp.jsonPath().getString("access");
                        Response msgResp = given()
                                .header("Authorization", "Bearer " + accessToken)
                                .get(apiBaseUrl + "messages/");

                        return msgResp.getStatusCode() == 200;
                    } catch (Exception e) {
                        return false;
                    }
                });
        System.out.println("Backend warmed up.");
    }

    public static Response executeWithColdStartRetry(Supplier<Response> requestSupplier) {
        return await()
                .atMost(120, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS)
                .ignoreExceptions() // optional: ignore connection refused etc.
                .until(requestSupplier::get,
                        response -> response.getStatusCode() < 500 && response.getStatusCode() != 0);
    }
}
