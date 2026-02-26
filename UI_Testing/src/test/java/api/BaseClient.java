package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.ConfigReader;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;

public class BaseClient {

    public static RequestSpecification baseSpec;

    static {
        // Read config
        String baseUrl = ConfigReader.getApiBaseUrl();

        // Base Spec (Public)
        baseSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    public static Response executeWithColdStartRetry(Supplier<Response> requestSupplier) {
        return await()
                .atMost(120, TimeUnit.SECONDS)
                .pollInterval(4, TimeUnit.SECONDS)
                .ignoreExceptions() // optional: ignore connection refused etc.
                .until(requestSupplier::get,
                        response -> response.getStatusCode() < 500 && response.getStatusCode() != 0);
    }

    /**
     * returns a RequestSpecification with Bearer token header
     */
    public static RequestSpecification getAuthSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(baseSpec)
                .addHeader("Authorization", "Bearer " + token)
                .build();
    }
}
