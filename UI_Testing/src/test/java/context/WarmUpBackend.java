package context;

import api.BaseClient;
import io.restassured.response.Response;
import utilities.ConfigReader;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

public class WarmUpBackend {

    public static void wakeUpBackendRender() {
        String pingUrl = ConfigReader.getProperty("api.base.url");
        System.out.println("Warming up Render backend via ping: " + pingUrl);
        await()
                .atMost(180, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> {

                    try {
                        Response resp = given()
                                .spec(BaseClient.baseSpec)
                                .get(pingUrl);
                        int statusCode = resp.getStatusCode();
                        System.out.println("Backend ping status: " + statusCode);
                        return statusCode == 200 || statusCode == 401 || statusCode == 403;
                    } catch (Exception e) {
                        System.out.println("Ping failed: " + e.getMessage());
                        return false;
                    }
                });
        System.out.println("Backend warmed up successfully");
    }
}
